package com.wzkris.auth.service.impl;

import com.wzkris.auth.domain.*;
import com.wzkris.auth.properties.TokenProperties;
import com.wzkris.auth.service.TokenService;
import com.wzkris.auth.utils.JwtTokenHelper;
import com.wzkris.auth.utils.TokenKeyBuilder;
import com.wzkris.common.core.model.DefaultLoginUser;
import com.wzkris.common.core.model.RoleContext;
import com.wzkris.common.core.support.LoginUser;
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

    private static final String HASH_FIELD_ROLES = "roles";

    private final TokenProperties tokenProperties;

    private final JwtTokenHelper jwtTokenHelper;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public TokenPair loginCreate(LoginUser loginUser, RoleContext roleContext) {
        return issue(loginUser, roleContext, UUID.randomUUID().toString(), SessionWriteOp.CREATE);
    }

    @Override
    public TokenPair loginReuse(LoginUser loginUser, RoleContext roleContext, Serializable sid) {
        return issue(loginUser, roleContext, sid, SessionWriteOp.REUSE);
    }

    @Override
    public TokenPair loginRefresh(LoginUser loginUser, RoleContext roleContext, String oldRefreshToken) {
        TokenClaims claims = jwtTokenHelper.parse(oldRefreshToken);
        String oldSid = claims.getSid();

        if (tokenProperties.getReuseRefreshTokens()) {
            Instant exp = claims.getExpiresAt();
            String refreshToken = ChronoUnit.HOURS.between(Instant.now(), exp) < 2
                    ? generateRefreshToken(loginUser, oldSid)
                    : oldRefreshToken;
            TokenPair pair = loginReuse(loginUser, roleContext, oldSid);
            return new TokenPair(pair.accessToken(), refreshToken);
        }

        revoke(loginUser.getAuthType().getValue(), loginUser.getUid(), oldSid);
        return loginCreate(loginUser, roleContext);
    }

    private TokenPair issue(LoginUser loginUser, RoleContext roleContext, Serializable sid, SessionWriteOp op) {
        persist(loginUser, sid, roleContext, op);
        return new TokenPair(generateAccessToken(loginUser, sid), generateRefreshToken(loginUser, sid));
    }

    private String generateAccessToken(LoginUser loginUser, Serializable sid) {
        return jwtTokenHelper.encodeLoginToken(
                tokenProperties.getAccessTokenTimeOut(),
                loginUser.getUid(),
                sid,
                loginUser.getAuthType());
    }

    private String generateRefreshToken(LoginUser loginUser, Serializable sid) {
        return jwtTokenHelper.encodeLoginToken(
                tokenProperties.getRefreshTokenTimeOut(),
                loginUser.getUid(),
                sid,
                loginUser.getAuthType());
    }

    private void persist(LoginUser loginUser, Serializable sid, RoleContext roleContext, SessionWriteOp op) {
        Serializable uid = loginUser.getUid();
        String type = loginUser.getAuthType().getValue();
        // 身份快照/会话元数据/会话索引统一以 refresh_token 有效期为过期时间，随会话上下文同生共死
        long ttl = tokenProperties.getRefreshTokenTimeOut();

        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        String sessionIndexKey = TokenKeyBuilder.buildSessionIndexKey(type, uid);
        String sessionEntryKey = TokenKeyBuilder.buildSessionEntryKey(type, uid, sid);

        redisTemplate.execute(new SessionCallback<Void>() {
            @Override
            @SuppressWarnings({"unchecked"})
            public Void execute(RedisOperations ops) {
                Map<String, Object> userinfoMap = new HashMap<>();
                userinfoMap.put(HASH_FIELD_USER, loginUser);
                userinfoMap.put(HASH_FIELD_ROLES, roleContext);

                ops.multi();
                // 身份快照
                ops.opsForHash().putAll(userInfoKey, userinfoMap);
                ops.expire(userInfoKey, ttl, TimeUnit.SECONDS);

                // 会话元数据：独立 key、随会话到期自动消失；CREATE 写入，REUSE 仅续期
                if (op == SessionWriteOp.CREATE) {
                    ops.opsForValue().set(sessionEntryKey, buildOnlineSession(), ttl, TimeUnit.SECONDS);
                } else {
                    ops.expire(sessionEntryKey, ttl, TimeUnit.SECONDS);
                }

                // 会话存活索引：score 即过期时间，是"会话是否存活"的唯一真相源
                long expireTimestamp = System.currentTimeMillis() + (ttl * 1000);
                ops.opsForZSet().add(sessionIndexKey, sid, expireTimestamp);
                ops.opsForZSet().removeRangeByScore(sessionIndexKey, 0, System.currentTimeMillis() - 1);
                ops.expire(sessionIndexKey, ttl, TimeUnit.SECONDS);
                ops.exec();
                return null;
            }
        });
    }

    @Override
    public UserContext loadUserContext(String type, Serializable uid) {
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);

        // 两段读取（HGET loginUser + HGET roles）合成一次 pipeline 往返，与 loadUserSessionContext 结构一致。
        List<Object> results = redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            @SuppressWarnings({"unchecked"})
            public Object execute(RedisOperations ops) {
                ops.opsForHash().get(userInfoKey, HASH_FIELD_USER);
                ops.opsForHash().get(userInfoKey, HASH_FIELD_ROLES);
                return null;
            }
        });

        DefaultLoginUser loginUser = results.get(0) instanceof DefaultLoginUser u ? u : null;
        RoleContext roleContext = results.get(1) instanceof RoleContext r ? r : null;
        return new UserContext(loginUser, roleContext);
    }

    @Override
    public UserSessionContext loadUserSessionContext(String type, Serializable uid, Serializable sid) {
        String sessionIndexKey = TokenKeyBuilder.buildSessionIndexKey(type, uid);
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);

        // 三段读取（ZSCORE 会话存活 + HGET loginUser + HGET roles）合成一次 pipeline 往返。
        // 两 key 均带 {type:uid} hash tag 落在同一 slot，pipeline 不会触发 CROSSSLOT。
        List<Object> results = redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            @SuppressWarnings({"unchecked"})
            public Object execute(RedisOperations ops) {
                ops.opsForZSet().score(sessionIndexKey, sid);
                ops.opsForHash().get(userInfoKey, HASH_FIELD_USER);
                ops.opsForHash().get(userInfoKey, HASH_FIELD_ROLES);
                return null;
            }
        });

        // sid 在会话索引中（score 存在）则未撤销，否则已撤销
        boolean revoked = !(results.get(0) instanceof Number);
        DefaultLoginUser loginUser = results.get(1) instanceof DefaultLoginUser u ? u : null;
        RoleContext roleContext = results.get(2) instanceof RoleContext r ? r : null;
        return new UserSessionContext(revoked, new UserContext(loginUser, roleContext));
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
        // 一次性多读各会话元数据 key
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

    @Override
    public void revoke(String type, Serializable uid, Serializable sid) {
        String sessionEntryKey = TokenKeyBuilder.buildSessionEntryKey(type, uid, sid);
        String sessionIndexKey = TokenKeyBuilder.buildSessionIndexKey(type, uid);
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);

        // 删会话元数据 + 从索引移除；索引清空则连同身份快照一并删除。
        // 三 key 均带 {type:uid} hash tag 同 slot，Lua 可原子执行。
        String luaScript =
                "redis.call('DEL', KEYS[1]) "
                        + "redis.call('ZREM', KEYS[2], ARGV[1]) "
                        + "redis.call('ZREMRANGEBYSCORE', KEYS[2], 0, ARGV[2]) "
                        + "if redis.call('ZCARD', KEYS[2]) == 0 then "
                        + "    redis.call('DEL', KEYS[2], KEYS[3]) "
                        + "end "
                        + "return 1";

        DefaultRedisScript<Long> script = new DefaultRedisScript<>(luaScript, Long.class);
        redisTemplate.execute(script, Arrays.asList(sessionEntryKey, sessionIndexKey, userInfoKey),
                sid, System.currentTimeMillis());
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
