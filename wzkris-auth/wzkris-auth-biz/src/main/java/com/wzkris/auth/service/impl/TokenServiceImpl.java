package com.wzkris.auth.service.impl;

import com.wzkris.auth.domain.OnlineSession;
import com.wzkris.auth.domain.TokenClaims;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
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

    /**
     * 生成 Access Token（JWT，含 uid、sid）。
     *
     * @param baseLoginUser 用户
     * @param sid           会话ID
     * @return Access Token（JWT），失败返回 null
     */
    @Override
    public String generateAccessToken(BaseLoginUser baseLoginUser, String sid) {
        Jwt jwt = getJwt(tokenProperties.getAccessTokenTimeOut(), baseLoginUser.getUid(), sid, baseLoginUser.getAuthType().getValue());
        return jwt.getTokenValue();
    }

    /**
     * 生成 Refresh Token（JWT，与 accessToken 同 sid）。
     *
     * @param baseLoginUser 用户
     * @param sid           会话ID
     * @return Refresh Token（JWT），失败返回 null
     */
    @Override
    public String generateRefreshToken(BaseLoginUser baseLoginUser, String sid) {
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

    /**
     * 保存用户信息与会话。
     *
     * @param baseLoginUser 用户信息
     * @param sid           会话ID
     * @param permissions   权限集合
     */
    @Override
    public void save(BaseLoginUser baseLoginUser, String sid, Set<String> permissions) {
        Serializable uid = baseLoginUser.getUid();
        String type = baseLoginUser.getAuthType().getValue();
        long refreshTTL = tokenProperties.getRefreshTokenTimeOut();

        OnlineSession onlineSession = buildOnlineSession();
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        // 保留原有 session key 作为会话索引（SET），单个会话条目使用独立 key
        String sessionIndexKey = TokenKeyBuilder.buildSessionIndexKey(type, uid);
        String sessionEntryKey = TokenKeyBuilder.buildSessionEntryKey(type, uid, sid);
        redisTemplate.execute(new SessionCallback<Void>() {
            @Override
            @SuppressWarnings({"unchecked"})
            public Void execute(RedisOperations operations) {
                operations.multi();
                operations.opsForHash().put(userInfoKey, HASH_FIELD_USER, baseLoginUser);
                operations.opsForHash().put(userInfoKey, HASH_FIELD_PERMISSIONS, permissions);
                operations.expire(userInfoKey, refreshTTL, TimeUnit.SECONDS);
                // 每个会话一个独立 key，利用 EXPIRE 自动过期
                operations.opsForValue().set(sessionEntryKey, onlineSession);
                operations.expire(sessionEntryKey, refreshTTL, TimeUnit.SECONDS);
                // 维护会话索引，便于列出/管理（索引允许有过期痕迹，读取时做懒删除）
                operations.opsForSet().add(sessionIndexKey, sid);
                operations.expire(sessionIndexKey, refreshTTL, TimeUnit.SECONDS);
                operations.exec();
                return null;
            }
        });
    }

    /**
     * 按 uid 获取用户信息。
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @return 用户信息，不存在返回 null
     */
    @Override
    public BaseLoginUser loadLoginUserByUid(String type, Serializable uid) {
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        return (BaseLoginUser) redisTemplate.opsForHash().get(userInfoKey, HASH_FIELD_USER);
    }

    /**
     * 按 uid 获取权限。
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @return 权限集合，不存在返回 null
     */
    @Override
    @SuppressWarnings("unchecked")
    public Set<String> loadPermissionsByUid(String type, Serializable uid) {
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        return (Set<String>) redisTemplate.opsForHash().get(userInfoKey, HASH_FIELD_PERMISSIONS);
    }

    /**
     * 按 sid 移除会话；若无其它会话则删除 userInfo/session key。
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @param sid  会话ID
     */
    @Override
    public void revoke(String type, Serializable uid, String sid) {
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        String sessionIndexKey = TokenKeyBuilder.buildSessionIndexKey(type, uid);
        String sessionEntryKey = TokenKeyBuilder.buildSessionEntryKey(type, uid, sid);

        // 删除会话条目与索引项
        redisTemplate.delete(List.of(sessionEntryKey));
        redisTemplate.opsForSet().remove(sessionIndexKey, sid);

        Long remain = redisTemplate.opsForSet().size(sessionIndexKey);
        if (remain == null || remain == 0) {
            // 无剩余会话，清理用户信息与索引
            redisTemplate.delete(List.of(userInfoKey, sessionIndexKey));
        }
    }

    /**
     * 检查 sid 是否已拉黑（不在会话中则视为已拉黑）。
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @param sid  会话ID
     * @return 已拉黑为 true，否则 false
     */
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

    /**
     * 按 uid 拉取在线会话 Map（sid -> OnlineSession）。
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @return sid -> OnlineSession，无会话返回空 Map
     */
    @Override
    public Map<String, OnlineSession> loadSessionCache(String type, Serializable uid) {
        String sessionIndexKey = TokenKeyBuilder.buildSessionIndexKey(type, uid);
        Set<Object> members = redisTemplate.opsForSet().members(sessionIndexKey);
        Map<String, OnlineSession> result = new HashMap<>();
        if (members == null || members.isEmpty()) {
            return result;
        }
        for (Object m : members) {
            String sid = String.valueOf(m);
            String sessionEntryKey = TokenKeyBuilder.buildSessionEntryKey(type, uid, sid);
            OnlineSession session = (OnlineSession) redisTemplate.opsForValue().get(sessionEntryKey);
            if (session != null) {
                result.put(sid, session);
            } else {
                // 懒惰清理索引中的过期 sid
                redisTemplate.opsForSet().remove(sessionIndexKey, sid);
            }
        }
        return result;
    }

}
