package com.thingslink.auth.oauth2.service;

import com.thingslink.common.redis.util.RedisUtil;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @date : 2024/2/23 10:03
 * @description : OAuth2AuthorizationService 是一个中心组件，新的授权被存储，现有的授权被查询。
 * 当遵循特定的协议流程时，它被其他组件使用例如，客户端认证、授权许可处理、令牌内省、令牌撤销、动态客户端注册等。
 */
@Slf4j
@Service
@AllArgsConstructor
public class OAuth2AuthorizationServiceImpl implements OAuth2AuthorizationService {

    private static final String PREFIX = "Authorization";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(@Nonnull OAuth2Authorization authorization) {
        // 所有code的过期时间，方便计算最大值
        List<Long> expiresAtList = new ArrayList<>();

        Map<String, Long> tokenMap = new HashMap<>();

        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        if (authorizationCode != null) {
            long between = ChronoUnit.SECONDS.between(authorizationCode.getToken().getIssuedAt(), authorizationCode.getToken().getExpiresAt());
            expiresAtList.add(between);
            tokenMap.put(this.buildRedisKey(authorizationCode.getToken().getTokenValue()), between);
        }

        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
        if (accessToken != null) {
            long between = ChronoUnit.SECONDS.between(accessToken.getToken().getIssuedAt(), accessToken.getToken().getExpiresAt());
            expiresAtList.add(between);
            tokenMap.put(this.buildRedisKey(accessToken.getToken().getTokenValue()), between);
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        if (refreshToken != null) {
            long between = ChronoUnit.SECONDS.between(refreshToken.getToken().getIssuedAt(), refreshToken.getToken().getExpiresAt());
            expiresAtList.add(between);
            tokenMap.put(this.buildRedisKey(refreshToken.getToken().getTokenValue()), between);
        }

        OAuth2Authorization.Token<OAuth2UserCode> userCodeToken = authorization.getToken(OAuth2UserCode.class);
        if (userCodeToken != null) {
            long between = ChronoUnit.SECONDS.between(userCodeToken.getToken().getIssuedAt(), userCodeToken.getToken().getExpiresAt());
            expiresAtList.add(between);
            tokenMap.put(this.buildRedisKey(userCodeToken.getToken().getTokenValue()), between);
        }

        OAuth2Authorization.Token<OAuth2DeviceCode> deviceCodeToken = authorization.getToken(OAuth2DeviceCode.class);
        if (deviceCodeToken != null) {
            long between = ChronoUnit.SECONDS.between(deviceCodeToken.getToken().getIssuedAt(), deviceCodeToken.getToken().getExpiresAt());
            expiresAtList.add(between);
            tokenMap.put(this.buildRedisKey(deviceCodeToken.getToken().getTokenValue()), between);
        }

        OAuth2Authorization.Token<OidcIdToken> oidcIdTokenToken = authorization.getToken(OidcIdToken.class);
        if (oidcIdTokenToken != null) {
            long between = ChronoUnit.SECONDS.between(oidcIdTokenToken.getToken().getIssuedAt(), oidcIdTokenToken.getToken().getExpiresAt());
            expiresAtList.add(between);
            tokenMap.put(this.buildRedisKey(oidcIdTokenToken.getToken().getTokenValue()), between);
        }

        Long maxTimeOut = expiresAtList.stream().max(Comparator.comparing(s -> s)).orElse(0L);

        SessionCallback<Object> sessionCallback = new SessionCallback<>() {
            @Override
            public <K, V> Object execute(@NotNull RedisOperations<K, V> operations) throws DataAccessException {
                for (Map.Entry<String, Long> entry : tokenMap.entrySet()) {
                    operations.opsForValue().set((K) entry.getKey(), (V) authorization.getId(), entry.getValue(), TimeUnit.SECONDS);
                }
                operations.opsForValue().set((K) buildRedisKey(authorization.getId()), (V) authorization, maxTimeOut, TimeUnit.SECONDS);
                return null;
            }
        };
        redisTemplate.executePipelined(sessionCallback);
    }

    @Override
    public void remove(@Nonnull OAuth2Authorization authorization) {
        List<String> keys = new ArrayList<>();
        keys.add(this.buildRedisKey(authorization.getId()));

        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        if (authorizationCode != null) {
            keys.add(this.buildRedisKey(authorizationCode.getToken().getTokenValue()));
        }

        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
        if (accessToken != null) {
            keys.add(this.buildRedisKey(accessToken.getToken().getTokenValue()));
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        if (refreshToken != null) {
            keys.add(this.buildRedisKey(refreshToken.getToken().getTokenValue()));
        }

        OAuth2Authorization.Token<OAuth2UserCode> userCodeToken = authorization.getToken(OAuth2UserCode.class);
        if (userCodeToken != null) {
            keys.add(this.buildRedisKey(userCodeToken.getToken().getTokenValue()));
        }

        OAuth2Authorization.Token<OAuth2DeviceCode> deviceCodeToken = authorization.getToken(OAuth2DeviceCode.class);
        if (deviceCodeToken != null) {
            keys.add(this.buildRedisKey(deviceCodeToken.getToken().getTokenValue()));
        }

        OAuth2Authorization.Token<OidcIdToken> oidcIdTokenToken = authorization.getToken(OidcIdToken.class);
        if (oidcIdTokenToken != null) {
            keys.add(this.buildRedisKey(oidcIdTokenToken.getToken().getTokenValue()));
        }

        RedisUtil.deleteObject(keys.toArray(String[]::new));
    }

    @Override
    public OAuth2Authorization findById(String id) {
        throw new UnsupportedOperationException(this.getClass().getName() + "#findById is not support");
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Object authorizeId = redisTemplate.opsForValue().get(this.buildRedisKey(token));
        if (authorizeId == null) {
            return null;
        }

        return (OAuth2Authorization) redisTemplate.opsForValue().get(this.buildRedisKey(authorizeId.toString()));
    }

    // 构建客户端缓存KEY
    private String buildRedisKey(String token) {
        return String.format("%s:%s", PREFIX, token);
    }
}
