package com.wzkris.auth.service.impl;

import com.wzkris.auth.domain.OnlineSession;
import com.wzkris.auth.domain.TokenClaims;
import com.wzkris.auth.domain.TokenPair;
import com.wzkris.auth.properties.TokenProperties;
import com.wzkris.auth.service.TokenService;
import com.wzkris.auth.utils.TokenKeyBuilder;
import com.wzkris.common.core.constant.JwtClaimConstants;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Token 操作服务，负责生成/解析/存储 JWT 及 Redis 会话。
 *
 * @author wzkris
 */
@Slf4j
@Component
public class TokenServiceImpl implements TokenService {

    /**
     * 用户信息 Hash 字段：用户对象
     */
    private static final String HASH_FIELD_USER = "loginUser";

    /**
     * 用户信息 Hash 字段：权限集合
     */
    private static final String HASH_FIELD_PERMISSIONS = "permissions";

    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AuthorizationServerSettings authorizationServerSettings;

    @Override
    public TokenPair login(BaseLoginUser loginUser, Set<String> permissions) {
        String sid = UUID.randomUUID().toString();
        String accessToken = generateAccessToken(loginUser, sid);
        String refreshToken = generateRefreshToken(loginUser, sid);
        save(loginUser, sid, permissions);
        return new TokenPair(accessToken, refreshToken);
    }

    @Override
    public TokenPair refresh(BaseLoginUser loginUser, Set<String> permissions, String oldRefreshToken) {
        TokenClaims claims = parseJwt(oldRefreshToken);
        String oldSid = claims.getSid();

        String refreshToken;
        String sid;

        if (tokenProperties.getReuseRefreshTokens()) {
            // 重用模式：保持 sid，不轮转，仅在接近过期时重新生成 refresh token
            sid = oldSid;
            Instant exp = claims.getExpiresAt();
            if (ChronoUnit.HOURS.between(Instant.now(), exp) < 2) {
                refreshToken = generateRefreshToken(loginUser, sid);
            } else {
                refreshToken = oldRefreshToken;
            }
            save(loginUser, sid, permissions);
        } else {
            // 轮转模式：生成新的 sid 与新的 refresh token，保存新会话并撤销旧会话
            sid = UUID.randomUUID().toString();
            refreshToken = generateRefreshToken(loginUser, sid);
            save(loginUser, sid, permissions);
            revoke(loginUser.getAuthType().getValue(), loginUser.getUid(), oldSid);
        }

        String accessToken = generateAccessToken(loginUser, sid);
        return new TokenPair(accessToken, refreshToken);
    }

    private String generateAccessToken(BaseLoginUser baseLoginUser, String sid) {
        Jwt jwt = getJwt(tokenProperties.getAccessTokenTimeOut(), baseLoginUser.getUid(), sid, baseLoginUser.getAuthType().getValue());
        return jwt.getTokenValue();
    }

    private String generateRefreshToken(BaseLoginUser baseLoginUser, String sid) {
        Jwt jwt = getJwt(tokenProperties.getRefreshTokenTimeOut(), baseLoginUser.getUid(), sid, baseLoginUser.getAuthType().getValue());
        return jwt.getTokenValue();
    }

    private Jwt getJwt(int timeout, Long uid, String sid, String authType) {
        JwsAlgorithm jwsAlgorithm = SignatureAlgorithm.RS256;

        JwsHeader jwsHeader = JwsHeader.with(jwsAlgorithm)
                .build();

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(Duration.ofSeconds(timeout));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(uid.toString())  // uid
                .claim(JwtClaimConstants.SID, sid)  // 会话ID，与 accessToken 的 sid 相同
                .claim(JwtClaimConstants.AUTH_TYPE, authType)
                .issuer(authorizationServerSettings.getIssuer())  // 添加 issuer
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .id(UUID.randomUUID().toString())
                .notBefore(issuedAt)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims));
    }

    /**
     * 从 JWT 解析 claims（统一 TokenClaims 访问模型）。
     *
     * @param token JWT（access 或 refresh）
     * @return TokenClaims
     */
    @Override
    public TokenClaims parseJwt(String token) {
        return TokenClaims.from(jwtDecoder.decode(token));
    }

    private void save(BaseLoginUser baseLoginUser, String sid, Set<String> permissions) {
        Serializable uid = baseLoginUser.getUid();
        String type = baseLoginUser.getAuthType().getValue();
        long refreshTTL = tokenProperties.getRefreshTokenTimeOut();

        OnlineSession onlineSession = buildOnlineSession();
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        String sessionIndexKey = TokenKeyBuilder.buildSessionIndexKey(type, uid);
        String sessionEntryKey = TokenKeyBuilder.buildSessionEntryKey(type, uid, sid);

        redisTemplate.execute(new SessionCallback<Void>() {
            @Override
            @SuppressWarnings({"unchecked"})
            public Void execute(RedisOperations ops) {
                Map<String, Object> userinfoMap = new HashMap<>();
                userinfoMap.put(HASH_FIELD_USER, baseLoginUser);
                userinfoMap.put(HASH_FIELD_PERMISSIONS, permissions);

                ops.multi();
                ops.opsForHash().putAll(userInfoKey, userinfoMap);
                ops.expire(userInfoKey, refreshTTL, TimeUnit.SECONDS);

                // 独立会话条目
                ops.opsForValue().set(sessionEntryKey, onlineSession, refreshTTL, TimeUnit.SECONDS);

                // 维护 ZSet 索引
                long expireTimestamp = System.currentTimeMillis() + (refreshTTL * 1000);
                ops.opsForZSet().add(sessionIndexKey, sid, expireTimestamp);

                // 【重要优化】：顺手清理已经自然过期的历史僵尸 sid，防止 ZSet 随着频繁登录无限膨胀
                ops.opsForZSet().removeRangeByScore(sessionIndexKey, 0, System.currentTimeMillis() - 1);

                ops.expire(sessionIndexKey, refreshTTL, TimeUnit.SECONDS);
                ops.exec();
                return null;
            }
        });
    }

    @Override
    public BaseLoginUser loadLoginUserByUid(String type, Serializable uid) {
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        return (BaseLoginUser) redisTemplate.opsForHash().get(userInfoKey, HASH_FIELD_USER);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<String> loadPermissionsByUid(String type, Serializable uid) {
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        return (Set<String>) redisTemplate.opsForHash().get(userInfoKey, HASH_FIELD_PERMISSIONS);
    }

    @Override
    public void revoke(String type, Serializable uid, String sid) {
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        String sessionIndexKey = TokenKeyBuilder.buildSessionIndexKey(type, uid);
        String sessionEntryKey = TokenKeyBuilder.buildSessionEntryKey(type, uid, sid);

        // 1. 删除会话条目本身
        redisTemplate.delete(sessionEntryKey);

        // 2. 从 ZSet 移除该 sid
        redisTemplate.opsForZSet().remove(sessionIndexKey, sid);

        // 3. 【重要优化】: 使用 Lua 脚本原子性地清理 userInfo，防止并发竞态条件导致误删
        String luaScript =
                "redis.call('ZREMRANGEBYSCORE', KEYS[1], 0, ARGV[1]) " + // 顺手清理僵尸会话
                        "if redis.call('ZCARD', KEYS[1]) == 0 then " +          // 如果真的空了
                        "    redis.call('DEL', KEYS[1], KEYS[2]) " +             // 原子性删除 userInfo 和 ZSet
                        "end " +
                        "return 1";

        DefaultRedisScript<Long> script = new DefaultRedisScript<>(luaScript, Long.class);

        redisTemplate.execute(script, Arrays.asList(sessionIndexKey, userInfoKey), System.currentTimeMillis());
    }

    @Override
    public boolean isRevoked(String type, Long uid, String sid) {
        String sessionEntryKey = TokenKeyBuilder.buildSessionEntryKey(type, uid, sid);
        Boolean exists = redisTemplate.hasKey(sessionEntryKey);
        return !exists;
    }

    /**
     * 从当前请求构建 OnlineSession。
     */
    private OnlineSession buildOnlineSession() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String clientIP = ServletUtil.getClientIP(request);
            UserAgent.ImmutableUserAgent userAgent = UserAgentUtil.INSTANCE.parse(request.getHeader(HttpHeaders.USER_AGENT));

            OnlineSession onlineSession = new OnlineSession();
            onlineSession.setDevice(userAgent.getValue(UserAgent.DEVICE_NAME));
            onlineSession.setDeviceBrand(userAgent.getValue(UserAgent.DEVICE_BRAND));
            onlineSession.setLoginIp(clientIP);
            onlineSession.setBrowser(userAgent.getValue(UserAgent.AGENT_NAME));
            onlineSession.setOs(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME));
            onlineSession.setLoginTime(new Date());
            return onlineSession;
        } catch (Exception e) {
            log.warn("Failed to build online session, returning empty session: {}", e.getMessage());
            return new OnlineSession();
        }
    }

    @Override
    public Map<String, OnlineSession> loadSessionCache(String type, Serializable uid) {
        // 1. 直接通过 Score 范围，仅获取当前时间戳之后（未过期）的 sid
        String sessionIndexKey = TokenKeyBuilder.buildSessionIndexKey(type, uid);
        long now = System.currentTimeMillis();
        Set<Object> members = redisTemplate.opsForZSet().rangeByScore(sessionIndexKey, now, Double.MAX_VALUE);

        Map<String, OnlineSession> result = new HashMap<>();
        if (CollectionUtils.isEmpty(members)) {
            return result;
        }

        List<String> sids = new ArrayList<>(members.size());
        List<String> entryKeys = new ArrayList<>(members.size());
        for (Object m : members) {
            String sid = String.valueOf(m);
            sids.add(sid);
            entryKeys.add(TokenKeyBuilder.buildSessionEntryKey(type, uid, sid));
        }
        List<Object> sessions = redisTemplate.opsForValue().multiGet(entryKeys);

        // 3. 遍历结果
        List<Object> dirtySids = new ArrayList<>();
        for (int i = 0; i < sids.size(); i++) {
            String sid = sids.get(i);
            OnlineSession session = (OnlineSession) (sessions != null ? sessions.get(i) : null);
            if (session != null) {
                result.put(sid, session);
            } else {
                // 极端边缘情况：String Key 被手动删了，但 ZSet 还没到期
                dirtySids.add(sid);
            }
        }

        // 4. 懒清理脏数据
        if (!dirtySids.isEmpty()) {
            redisTemplate.opsForZSet().remove(sessionIndexKey, dirtySids.toArray());
        }

        return result;
    }

}
