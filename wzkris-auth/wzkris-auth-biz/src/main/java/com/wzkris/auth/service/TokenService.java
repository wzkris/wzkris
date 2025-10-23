package com.wzkris.auth.service;

import com.wzkris.auth.domain.OnlineUser;
import com.wzkris.auth.security.config.TokenProperties;
import com.wzkris.common.core.model.CorePrincipal;
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

    private final String TOKEN_PREFIX = "auth-token:{%s}";

    private final String ONLINE_PREFIX = "auth-token:{%s}:online";

    private final String ACCESS_TOKEN_PREFIX = "auth-token:access-token:";

    private final String REFRESH_TOKEN_PREFIX = "auth-token:refresh-token:";

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private TokenProperties tokenProperties;

    private String buildInfoKey(Serializable id) {
        return TOKEN_PREFIX.formatted(id);
    }

    private String buildOnlineKey(Serializable id) {
        return ONLINE_PREFIX.formatted(id);
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
        Serializable id = principal.getId();
        long refreshTTL = tokenProperties.getUserRefreshTokenTimeOut();
        long accessTTL = tokenProperties.getUserTokenTimeOut();

        // ========== 1️⃣ 批量写入用户信息 + 在线会话 ==========
        RBatch batch = redissonClient.createBatch();

        // 保存用户主信息（带过期）
        RBucketAsync<CorePrincipal> userinfo = batch.getBucket(buildInfoKey(id));
        userinfo.setAsync(principal, Duration.ofSeconds(refreshTTL));

        // 处理在线会话 - 使用批量操作检查是否存在
        RMapCacheAsync<String, OnlineUser> onlineCacheAsync = batch.getMapCache(buildOnlineKey(id));
        // 先检查是否存在，如果存在则刷新过期时间，否则新建
        onlineCacheAsync.containsKeyAsync(refreshToken).thenAccept(exists -> {
            if (exists) {
                // 已存在则仅刷新过期时间
                onlineCacheAsync.expireEntryAsync(refreshToken, Duration.ofSeconds(refreshTTL), Duration.ZERO);
            } else {
                // 新建会话
                onlineCacheAsync.putAsync(refreshToken, new OnlineUser(), refreshTTL, TimeUnit.SECONDS);
            }
        });

        // 执行用户相关数据的批量操作（同一分片）
        batch.executeAsync();

        // ========== 2️⃣ 处理Token映射（不同分片，单独操作） ==========
        RBucket<TokenBody> accessTokenBucket = redissonClient.getBucket(buildAccessTokenKey(accessToken));
        accessTokenBucket.setAsync(new TokenBody(id, refreshToken), Duration.ofSeconds(accessTTL));

        // 立刻失效旧 access_token（若存在）
        RBucket<TokenBody> refreshTokenBucket = redissonClient.getBucket(buildRefreshTokenKey(refreshToken));
        TokenBody oldToken = refreshTokenBucket.get();
        if (oldToken != null) {
            redissonClient.getBucket(buildAccessTokenKey(oldToken.getToken())).deleteAsync();
        }

        // 更新 refresh_token 关联的 access_token
        refreshTokenBucket.setAsync(new TokenBody(id, accessToken), Duration.ofSeconds(refreshTTL));
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

        return (CorePrincipal) redissonClient.getBucket(buildInfoKey(tokenBody.getId())).get();
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

        return (CorePrincipal) redissonClient.getBucket(buildInfoKey(tokenBody.getId())).get();
    }

    /**
     * token反查
     *
     * @param accessToken token
     */
    public final String loadRefreshTokenByAccessToken(String accessToken) {
        TokenBody tokenBody = (TokenBody) redissonClient.getBucket(buildAccessTokenKey(accessToken)).get();
        return tokenBody.getToken();
    }

    /**
     * token反查
     *
     * @param refreshToken token
     */
    public final String loadAccessTokenByRefreshToken(String refreshToken) {
        TokenBody tokenBody = (TokenBody) redissonClient.getBucket(buildRefreshTokenKey(refreshToken)).get();
        return tokenBody.getToken();
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
        String refreshToken = tokenBody.getToken();

        // 删除Token映射（不同分片，单独操作）
        redissonClient.getBucket(buildAccessTokenKey(accessToken)).delete();
        redissonClient.getBucket(buildRefreshTokenKey(refreshToken)).delete();

        // ========== 批量操作删除用户相关数据（同一分片） ==========
        RBatch batch = redissonClient.createBatch();

        // 删除在线会话
        RMapCacheAsync<String, Object> onlineCacheAsync = batch.getMapCache(buildOnlineKey(id));
        onlineCacheAsync.removeAsync(refreshToken);

        // 检查是否还有其他会话，如果没有则删除用户信息
        onlineCacheAsync.sizeAsync().thenAccept(size -> {
            if (size == 0) {
                batch.getBucket(buildInfoKey(id)).deleteAsync();
            }
        });

        // 执行批量删除操作
        batch.executeAsync();
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
        String accessToken = tokenBody.getToken();

        // 删除Token映射（不同分片，单独操作）
        redissonClient.getBucket(buildAccessTokenKey(accessToken)).delete();
        redissonClient.getBucket(buildRefreshTokenKey(refreshToken)).delete();

        // ========== 批量操作删除用户相关数据（同一分片） ==========
        RBatch batch = redissonClient.createBatch();

        // 删除在线会话
        RMapCacheAsync<String, Object> onlineCacheAsync = batch.getMapCache(buildOnlineKey(id));
        onlineCacheAsync.removeAsync(refreshToken);

        // 检查是否还有其他会话，如果没有则删除用户信息
        onlineCacheAsync.sizeAsync().thenAccept(size -> {
            if (size == 0) {
                batch.getBucket(buildInfoKey(id)).deleteAsync();
            }
        });

        // 执行批量删除操作
        batch.executeAsync();
    }

    /**
     * 根据用户ID获取在线会话列表
     *
     * @param userId 用户ID
     */
    public final RMapCache<String, OnlineUser> getOnlineCache(Serializable userId) {
        return redissonClient.getMapCache(buildOnlineKey(userId));
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
        private String token;

        public TokenBody() {
        }

        private TokenBody(Serializable id, String token) {
            this.id = id;
            this.token = token;
        }

    }

}
