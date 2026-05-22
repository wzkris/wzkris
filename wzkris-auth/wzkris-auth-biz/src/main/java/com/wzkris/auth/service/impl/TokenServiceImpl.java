package com.wzkris.auth.service.impl;

import com.wzkris.auth.domain.OnlineSession;
import com.wzkris.auth.domain.TokenClaims;
import com.wzkris.auth.domain.TokenPair;
import com.wzkris.auth.properties.TokenProperties;
import com.wzkris.auth.service.TokenService;
import com.wzkris.auth.utils.JwtTokenHelper;
import com.wzkris.auth.utils.TokenKeyBuilder;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Token 会话服务：Redis 会话存储与登录令牌编排。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private static final String HASH_FIELD_USER = "loginUser";

    private static final String HASH_FIELD_PERMISSIONS = "permissions";

    private final TokenProperties tokenProperties;

    private final JwtTokenHelper jwtTokenHelper;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public TokenPair loginCreate(BaseLoginUser loginUser, Set<String> permissions) {
        return issue(loginUser, permissions, UUID.randomUUID().toString(), SessionWriteOp.CREATE);
    }

    @Override
    public TokenPair loginReuse(BaseLoginUser loginUser, Set<String> permissions, String sid) {
        return issue(loginUser, permissions, sid, SessionWriteOp.REUSE);
    }

    @Override
    public TokenPair loginRefresh(BaseLoginUser loginUser, Set<String> permissions, String oldRefreshToken) {
        TokenClaims claims = jwtTokenHelper.parse(oldRefreshToken);
        String oldSid = claims.getSid();

        if (tokenProperties.getReuseRefreshTokens()) {
            Instant exp = claims.getExpiresAt();
            String refreshToken = ChronoUnit.HOURS.between(Instant.now(), exp) < 2
                    ? generateRefreshToken(loginUser, oldSid)
                    : oldRefreshToken;
            TokenPair pair = loginReuse(loginUser, permissions, oldSid);
            return new TokenPair(pair.accessToken(), refreshToken);
        }

        revoke(loginUser.getAuthType().getValue(), loginUser.getUid(), oldSid);
        return loginCreate(loginUser, permissions);
    }

    private TokenPair issue(BaseLoginUser loginUser, Set<String> permissions, String sid, SessionWriteOp op) {
        persist(loginUser, sid, permissions, op);
        return new TokenPair(generateAccessToken(loginUser, sid), generateRefreshToken(loginUser, sid));
    }

    private String generateAccessToken(BaseLoginUser loginUser, String sid) {
        return jwtTokenHelper.encodeLoginToken(
                tokenProperties.getAccessTokenTimeOut(),
                loginUser.getUid(),
                sid,
                loginUser.getAuthType());
    }

    private String generateRefreshToken(BaseLoginUser loginUser, String sid) {
        return jwtTokenHelper.encodeLoginToken(
                tokenProperties.getRefreshTokenTimeOut(),
                loginUser.getUid(),
                sid,
                loginUser.getAuthType());
    }

    private void persist(BaseLoginUser loginUser, String sid, Set<String> permissions, SessionWriteOp op) {
        Serializable uid = loginUser.getUid();
        String type = loginUser.getAuthType().getValue();
        long refreshTTL = tokenProperties.getRefreshTokenTimeOut();

        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        String sessionIndexKey = TokenKeyBuilder.buildSessionIndexKey(type, uid);
        String sessionEntryKey = TokenKeyBuilder.buildSessionEntryKey(type, uid, sid);

        redisTemplate.execute(new SessionCallback<Void>() {
            @Override
            @SuppressWarnings({"unchecked"})
            public Void execute(RedisOperations ops) {
                Map<String, Object> userinfoMap = new HashMap<>();
                userinfoMap.put(HASH_FIELD_USER, loginUser);
                userinfoMap.put(HASH_FIELD_PERMISSIONS, permissions);

                ops.multi();
                ops.opsForHash().putAll(userInfoKey, userinfoMap);
                ops.expire(userInfoKey, refreshTTL, TimeUnit.SECONDS);

                if (op == SessionWriteOp.CREATE) {
                    ops.opsForValue().set(sessionEntryKey, buildOnlineSession(), refreshTTL, TimeUnit.SECONDS);
                } else {
                    ops.expire(sessionEntryKey, refreshTTL, TimeUnit.SECONDS);
                }

                long expireTimestamp = System.currentTimeMillis() + (refreshTTL * 1000);
                ops.opsForZSet().add(sessionIndexKey, sid, expireTimestamp);
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

        redisTemplate.delete(sessionEntryKey);
        redisTemplate.opsForZSet().remove(sessionIndexKey, sid);

        String luaScript =
                "redis.call('ZREMRANGEBYSCORE', KEYS[1], 0, ARGV[1]) "
                        + "if redis.call('ZCARD', KEYS[1]) == 0 then "
                        + "    redis.call('DEL', KEYS[1], KEYS[2]) "
                        + "end "
                        + "return 1";

        DefaultRedisScript<Long> script = new DefaultRedisScript<>(luaScript, Long.class);
        redisTemplate.execute(script, Arrays.asList(sessionIndexKey, userInfoKey), System.currentTimeMillis());
    }

    @Override
    public boolean isRevoked(String type, Long uid, String sid) {
        String sessionEntryKey = TokenKeyBuilder.buildSessionEntryKey(type, uid, sid);
        return !redisTemplate.hasKey(sessionEntryKey);
    }

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
            onlineSession.setLoginTime(OffsetDateTime.now());
            return onlineSession;
        } catch (Exception e) {
            log.warn("Failed to build online session, returning empty session: {}", e.getMessage());
            return new OnlineSession();
        }
    }

    @Override
    public Map<String, OnlineSession> loadSessionCache(String type, Serializable uid) {
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

        List<Object> dirtySids = new ArrayList<>();
        for (int i = 0; i < sids.size(); i++) {
            String sid = sids.get(i);
            OnlineSession session = (OnlineSession) (sessions != null ? sessions.get(i) : null);
            if (session != null) {
                result.put(sid, session);
            } else {
                dirtySids.add(sid);
            }
        }

        if (!dirtySids.isEmpty()) {
            redisTemplate.opsForZSet().remove(sessionIndexKey, dirtySids.toArray());
        }

        return result;
    }

    private enum SessionWriteOp {
        /**
         * 写入新的 OnlineSession 元数据
         */
        CREATE,
        /**
         * 仅续期已有 OnlineSession，不覆盖设备/IP 等元数据
         */
        REUSE
    }

}
