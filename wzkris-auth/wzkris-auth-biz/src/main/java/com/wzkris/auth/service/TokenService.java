package com.wzkris.auth.service;

import com.wzkris.auth.domain.OnlineUser;
import com.wzkris.auth.security.config.TokenProperties;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.redis.util.RedisUtil;
import jakarta.annotation.Nullable;
import lombok.Data;
import org.redisson.api.*;
import org.redisson.api.options.KeysScanOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * token操作
 *
 * @author wzkris
 */
@Component
public class TokenService {

    private final String GET_CORE_USER_SCRIPT = """
            local tokenData = redis.call('GET', KEYS[1])
            
            if not tokenData then
                return nil
            end
            
            local tokenObj = cjson.decode(tokenData)
            local userId = tokenObj.id
            
            return redis.call('GET', KEYS[2] .. userId)
            """;

    private final RedissonClient redissonClient = RedisUtil.getClient();

    private final String USER_INFO_PREFIX = "user_info:";

    private final String ONLINE_USER_PREFIX = "user_info:online:";

    private final String ACCESS_TOKEN_PREFIX = "user_info:access_token:";

    private final String REFRESH_TOKEN_PREFIX = "user_info:refresh_token:";

    @Autowired
    private TokenProperties tokenProperties;

    /**
     * 保存token及用户信息
     *
     * @param principal    用户信息
     * @param accessToken  token
     * @param refreshToken token
     */
    public final void save(CorePrincipal principal, String accessToken, String refreshToken) {
        RBatch batch = redissonClient.createBatch();

        RBucketAsync<CorePrincipal> userinfo = batch.getBucket(USER_INFO_PREFIX + principal.getId());
        userinfo.setAsync(principal, Duration.ofSeconds(tokenProperties.getUserRefreshTokenTimeOut()));

        if (StringUtil.isNotBlank(accessToken)) {
            RBucketAsync<TokenBody> accessTokenBucket = batch.getBucket(ACCESS_TOKEN_PREFIX + accessToken);
            accessTokenBucket.setAsync(new TokenBody(principal.getId(), refreshToken), Duration.ofSeconds(tokenProperties.getUserTokenTimeOut()));
        }

        if (StringUtil.isNotBlank(refreshToken)) {
            RBucketAsync<TokenBody> userRefreshTokenBucket = batch.getBucket(REFRESH_TOKEN_PREFIX + refreshToken);
            userRefreshTokenBucket.setAsync(new TokenBody(principal.getId(), accessToken), Duration.ofSeconds(tokenProperties.getUserRefreshTokenTimeOut()));
        }

        batch.execute();
    }

    /**
     * 根据token获取用户信息
     *
     * @param accessToken token
     */
    @Nullable
    public final CorePrincipal loadByAccessToken(String accessToken) {
        return redissonClient.getScript().eval(RScript.Mode.READ_ONLY, GET_CORE_USER_SCRIPT,
                RScript.ReturnType.VALUE, List.of(ACCESS_TOKEN_PREFIX + accessToken, USER_INFO_PREFIX));
    }

    /**
     * 根据token获取用户信息
     *
     * @param refreshToken token
     */
    @Nullable
    public final CorePrincipal loadByRefreshToken(String refreshToken) {
        return redissonClient.getScript().eval(RScript.Mode.READ_ONLY, GET_CORE_USER_SCRIPT,
                RScript.ReturnType.VALUE, List.of(REFRESH_TOKEN_PREFIX + refreshToken, USER_INFO_PREFIX));
    }

    /**
     * token反查
     *
     * @param accessToken token
     */
    public final String loadRefreshTokenByAccessToken(String accessToken) {
        TokenBody tokenBody = (TokenBody) redissonClient.getBucket(ACCESS_TOKEN_PREFIX + accessToken).get();
        return tokenBody.getOtherToken();
    }

    /**
     * token反查
     *
     * @param refreshToken token
     */
    public final String loadAccessTokenByRefreshToken(String refreshToken) {
        TokenBody tokenBody = (TokenBody) redissonClient.getBucket(REFRESH_TOKEN_PREFIX + refreshToken).get();
        return tokenBody.getOtherToken();
    }

    /**
     * 根据token移除信息
     *
     * @param accessToken token
     */
    public final void logoutByAccessToken(String accessToken) {
        TokenBody tokenBody = (TokenBody) redissonClient.getBucket(ACCESS_TOKEN_PREFIX + accessToken).get();
        String refreshToken = tokenBody.getOtherToken();

        redissonClient.getKeys().delete(ACCESS_TOKEN_PREFIX + accessToken, REFRESH_TOKEN_PREFIX + refreshToken);

        Iterable<String> keys = redissonClient.getKeys()
                .getKeys(KeysScanOptions.defaults().pattern(REFRESH_TOKEN_PREFIX + "*").limit(1));

        if (!keys.iterator().hasNext()) {
            redissonClient.getKeys().delete(USER_INFO_PREFIX + tokenBody.getId());
        }
    }

    /**
     * 根据token移除信息
     *
     * @param refreshToken token
     */
    public final void logoutByRefreshToken(String refreshToken) {
        TokenBody tokenBody = (TokenBody) redissonClient.getBucket(REFRESH_TOKEN_PREFIX + refreshToken).get();
        String accessToken = tokenBody.getOtherToken();

        redissonClient.getKeys().delete(ACCESS_TOKEN_PREFIX + accessToken, REFRESH_TOKEN_PREFIX + refreshToken);

        Iterable<String> keys = redissonClient.getKeys()
                .getKeys(KeysScanOptions.defaults().pattern(REFRESH_TOKEN_PREFIX + "*").limit(1));

        if (!keys.iterator().hasNext()) {
            redissonClient.getKeys().delete(USER_INFO_PREFIX + tokenBody.getId());
        }
    }

    /**
     * 根据用户ID获取在线会话列表
     *
     * @param userId 用户ID
     */
    public final RMapCache<String, OnlineUser> getOnlineCache(Object userId) {
        return redissonClient.getMapCache(ONLINE_USER_PREFIX + userId);
    }

    /**
     * 添加会话
     *
     * @param userId     用户ID
     * @param onlineUser 会话信息
     */
    public final void putOnlineSession(Object userId, OnlineUser onlineUser) {
        RMapCache<String, OnlineUser> onlineCache = getOnlineCache(userId);
        onlineCache.put(
                onlineUser.getRefreshToken(), onlineUser, tokenProperties.getRefreshTokenTimeOut(), TimeUnit.SECONDS);
    }

    /**
     * 延长会话过期时间
     *
     * @param userId       用户ID
     * @param refreshToken token
     */
    public final void expireOnlineSession(Object userId, String refreshToken) {
        RMapCache<String, OnlineUser> onlineCache = getOnlineCache(userId);
        onlineCache.expireEntry(
                refreshToken,
                Duration.ofSeconds(tokenProperties.getRefreshTokenTimeOut()),
                Duration.ofSeconds(0));
    }

    /**
     * 移除在线会话
     *
     * @param userId       用户ID
     * @param refreshToken token
     */
    public final void kickoutOnlineSessionByRefreshToken(Object userId, String refreshToken) {
        RMapCache<String, OnlineUser> onlineCache = getOnlineCache(userId);
        onlineCache.remove(refreshToken);
    }

    /**
     * 移除在线会话
     *
     * @param userId      用户ID
     * @param accessToken token
     */
    public final void kickoutOnlineSessionByAccessToken(Object userId, String accessToken) {
        TokenBody tokenBody = (TokenBody) redissonClient.getBucket(ACCESS_TOKEN_PREFIX + accessToken).get();
        String refreshToken = tokenBody.getOtherToken();

        RMapCache<String, OnlineUser> onlineCache = getOnlineCache(userId);
        onlineCache.remove(refreshToken);
    }

    @Data
    private static class TokenBody {

        /**
         * 用户ID
         */
        private String id;

        /**
         * 另外的TOKEN
         */
        private String otherToken;

        public TokenBody() {
        }

        private TokenBody(String id, String otherToken) {
            this.id = id;
            this.otherToken = otherToken;
        }

    }

}
