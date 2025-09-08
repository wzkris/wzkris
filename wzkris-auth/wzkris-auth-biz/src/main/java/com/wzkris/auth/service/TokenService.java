package com.wzkris.auth.service;

import com.wzkris.auth.domain.OnlineUser;
import com.wzkris.auth.security.config.TokenProperties;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.redis.util.RedisUtil;
import jakarta.annotation.Nullable;
import lombok.Data;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * token操作
 *
 * @author wzkris
 */
@Component
public class TokenService {

    private final RedissonClient redissonClient = RedisUtil.getClient();

    private final String USER_INFO_PREFIX = "user_info:{%s}";

    private final String ONLINE_USER_PREFIX = "user_info:{%s}:online";

    private final String ACCESS_TOKEN_PREFIX = "user_info:access_token:";

    private final String REFRESH_TOKEN_PREFIX = "user_info:refresh_token:";

    @Autowired
    private TokenProperties tokenProperties;

    private String buildUserInfoKey(Serializable id) {
        return USER_INFO_PREFIX.formatted(id);
    }

    private String buildOnlineUserKey(Serializable id) {
        return ONLINE_USER_PREFIX.formatted(id);
    }

    private String buildAccessTokenKey(String accessToken) {
        return ACCESS_TOKEN_PREFIX + accessToken;
    }

    private String buildRefreshTokenKey(String refreshToken) {
        return REFRESH_TOKEN_PREFIX + refreshToken;
    }

    /**
     * 保存token及用户信息
     *
     * @param principal    用户信息
     * @param accessToken  token
     * @param refreshToken token
     */
    public final void save(CorePrincipal principal, String accessToken, String refreshToken) {
        RBatch batch = redissonClient.createBatch();

        RBucketAsync<CorePrincipal> userinfo = batch.getBucket(buildUserInfoKey(principal.getId()));
        userinfo.setAsync(principal, Duration.ofSeconds(tokenProperties.getUserRefreshTokenTimeOut()));

        RMapCacheAsync<String, OnlineUser> mapCache = batch.getMapCache(buildOnlineUserKey(principal.getId()));
        mapCache.getAsync(refreshToken).thenAccept(onlineUser -> {
            if (onlineUser == null) {
                mapCache.putAsync(refreshToken, new OnlineUser(),
                        tokenProperties.getUserRefreshTokenTimeOut(), TimeUnit.SECONDS);
            } else {
                mapCache.expireEntryAsync(refreshToken, Duration.ofSeconds(tokenProperties.getUserRefreshTokenTimeOut()),
                        Duration.ofSeconds(0));
            }
        });

        batch.execute();

        RBucket<TokenBody> accessTokenBucket = redissonClient.getBucket(buildAccessTokenKey(accessToken));
        accessTokenBucket.setAsync(new TokenBody(principal.getId(), refreshToken),
                Duration.ofSeconds(tokenProperties.getUserTokenTimeOut()));

        // 立刻失效以前的access_token
        RBucket<TokenBody> refreshTokenBucket = redissonClient.getBucket(buildRefreshTokenKey(refreshToken));
        TokenBody tokenBody = refreshTokenBucket.get();
        if (tokenBody != null) {
            redissonClient.getBucket(buildAccessTokenKey(tokenBody.getOtherToken())).deleteAsync();
        }

        refreshTokenBucket.setAsync(new TokenBody(principal.getId(), accessToken),
                Duration.ofSeconds(tokenProperties.getUserRefreshTokenTimeOut()));
    }

    /**
     * 根据token获取用户信息
     *
     * @param accessToken token
     */
    @Nullable
    public final CorePrincipal loadByAccessToken(String accessToken) {
        TokenBody tokenBody = (TokenBody) redissonClient.getBucket(buildAccessTokenKey(accessToken)).get();
        if (tokenBody == null) return null;

        return (CorePrincipal) redissonClient.getBucket(buildUserInfoKey(tokenBody.getId())).get();
    }

    /**
     * 根据token获取用户信息
     *
     * @param refreshToken token
     */
    @Nullable
    public final CorePrincipal loadByRefreshToken(String refreshToken) {
        TokenBody tokenBody = (TokenBody) redissonClient.getBucket(buildRefreshTokenKey(refreshToken)).get();
        if (tokenBody == null) return null;

        return (CorePrincipal) redissonClient.getBucket(buildUserInfoKey(tokenBody.getId())).get();
    }

    /**
     * token反查
     *
     * @param accessToken token
     */
    public final String loadRefreshTokenByAccessToken(String accessToken) {
        TokenBody tokenBody = (TokenBody) redissonClient.getBucket(buildAccessTokenKey(accessToken)).get();
        return tokenBody.getOtherToken();
    }

    /**
     * token反查
     *
     * @param refreshToken token
     */
    public final String loadAccessTokenByRefreshToken(String refreshToken) {
        TokenBody tokenBody = (TokenBody) redissonClient.getBucket(buildRefreshTokenKey(refreshToken)).get();
        return tokenBody.getOtherToken();
    }

    /**
     * 根据token移除信息
     *
     * @param accessToken token
     */
    public final Serializable logoutByAccessToken(String accessToken) {
        TokenBody tokenBody = (TokenBody) redissonClient.getBucket(buildAccessTokenKey(accessToken)).get();
        if (tokenBody == null) return null;

        Serializable id = tokenBody.getId();
        String refreshToken = tokenBody.getOtherToken();

        // 删除Token（独立键）
        redissonClient.getBucket(buildAccessTokenKey(accessToken)).delete();
        redissonClient.getBucket(buildRefreshTokenKey(refreshToken)).delete();

        // 创建批量操作对象
        RBatch batch = redissonClient.createBatch();

        // 删除会话
        RMapCacheAsync<String, Object> mapCacheAsync = batch.getMapCache(buildOnlineUserKey(id));

        mapCacheAsync.removeAsync(refreshToken);

        mapCacheAsync.sizeAsync().thenAccept(size -> {
            if (size == 0) {
                redissonClient.getBucket(buildUserInfoKey(tokenBody.getId())).delete();
            }
        });

        batch.execute();
        return id;
    }

    /**
     * 根据token移除信息
     *
     * @param refreshToken token
     */
    public final void logoutByRefreshToken(String refreshToken) {
        TokenBody tokenBody = (TokenBody) redissonClient.getBucket(buildRefreshTokenKey(refreshToken)).get();
        if (tokenBody == null) return;

        Serializable id = tokenBody.getId();
        String accessToken = tokenBody.getOtherToken();

        // 删除Token（独立键）
        redissonClient.getBucket(buildAccessTokenKey(accessToken)).delete();
        redissonClient.getBucket(buildRefreshTokenKey(refreshToken)).delete();

        // 创建批量操作对象
        RBatch batch = redissonClient.createBatch();

        // 删除会话
        RMapCacheAsync<String, Object> mapCacheAsync = batch.getMapCache(buildOnlineUserKey(id));

        mapCacheAsync.removeAsync(refreshToken);

        mapCacheAsync.sizeAsync().thenAccept(size -> {
            if (size == 0) {
                redissonClient.getBucket(buildUserInfoKey(tokenBody.getId())).delete();
            }
        });

        batch.execute();
    }

    /**
     * 根据用户ID获取在线会话列表
     *
     * @param userId 用户ID
     */
    public final RMapCache<String, OnlineUser> getOnlineCache(Serializable userId) {
        return redissonClient.getMapCache(buildOnlineUserKey(userId));
    }

    /**
     * 添加会话
     *
     * @param userId     用户ID
     * @param onlineUser 会话信息
     */
    public final void putOnlineSession(Serializable userId, String refreshToken, OnlineUser onlineUser) {
        RMapCache<String, OnlineUser> onlineCache = getOnlineCache(userId);
        onlineCache.put(refreshToken, onlineUser, tokenProperties.getUserRefreshTokenTimeOut(), TimeUnit.SECONDS);
    }

    @Data
    private static class TokenBody {

        /**
         * 用户ID
         */
        private Serializable id;

        /**
         * 另外的TOKEN
         */
        private String otherToken;

        public TokenBody() {
        }

        private TokenBody(Serializable id, String otherToken) {
            this.id = id;
            this.otherToken = otherToken;
        }

    }

}
