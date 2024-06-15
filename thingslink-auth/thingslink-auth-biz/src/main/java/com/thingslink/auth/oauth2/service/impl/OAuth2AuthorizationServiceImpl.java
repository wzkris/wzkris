package com.thingslink.auth.oauth2.service.impl;

import com.thingslink.auth.oauth2.redis.JdkRedisUtil;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBatch;
import org.redisson.api.RedissonClient;
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

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    private static final String OAUTH2_PREFIX = "Authorization";

    @Override
    public void save(@Nonnull OAuth2Authorization authorization) {
        // 所有code的过期时间，方便计算最大值
        List<Long> expiresAtList = new ArrayList<>();

        // 判断所有OAuth2的code，如果有就存起来
        Map<String, Long> tokenMap = new HashMap<>();

        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        if (authorizationCode != null) {
            long between = ChronoUnit.SECONDS.between(authorizationCode.getToken().getIssuedAt(), authorizationCode.getToken().getExpiresAt());
            expiresAtList.add(between);
            tokenMap.put(this.buildOAuth2Key(authorizationCode.getToken().getTokenValue()), between);
        }

        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
        if (accessToken != null) {
            long between = ChronoUnit.SECONDS.between(accessToken.getToken().getIssuedAt(), accessToken.getToken().getExpiresAt());
            expiresAtList.add(between);
            tokenMap.put(this.buildOAuth2Key(accessToken.getToken().getTokenValue()), between);
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        if (refreshToken != null) {
            long between = ChronoUnit.SECONDS.between(refreshToken.getToken().getIssuedAt(), refreshToken.getToken().getExpiresAt());
            expiresAtList.add(between);
            tokenMap.put(this.buildOAuth2Key(refreshToken.getToken().getTokenValue()), between);
        }

        OAuth2Authorization.Token<OAuth2UserCode> userCodeToken = authorization.getToken(OAuth2UserCode.class);
        if (userCodeToken != null) {
            long between = ChronoUnit.SECONDS.between(userCodeToken.getToken().getIssuedAt(), userCodeToken.getToken().getExpiresAt());
            expiresAtList.add(between);
            tokenMap.put(this.buildOAuth2Key(userCodeToken.getToken().getTokenValue()), between);
        }

        OAuth2Authorization.Token<OAuth2DeviceCode> deviceCodeToken = authorization.getToken(OAuth2DeviceCode.class);
        if (deviceCodeToken != null) {
            long between = ChronoUnit.SECONDS.between(deviceCodeToken.getToken().getIssuedAt(), deviceCodeToken.getToken().getExpiresAt());
            expiresAtList.add(between);
            tokenMap.put(this.buildOAuth2Key(deviceCodeToken.getToken().getTokenValue()), between);
        }

        OAuth2Authorization.Token<OidcIdToken> oidcIdTokenToken = authorization.getToken(OidcIdToken.class);
        if (oidcIdTokenToken != null) {
            long between = ChronoUnit.SECONDS.between(oidcIdTokenToken.getToken().getIssuedAt(), oidcIdTokenToken.getToken().getExpiresAt());
            expiresAtList.add(between);
            tokenMap.put(this.buildOAuth2Key(oidcIdTokenToken.getToken().getTokenValue()), between);
        }

        Long maxTimeOut = expiresAtList.stream().max(Comparator.comparing(s -> s)).orElse(0L);
        // 创建批量命令
        RBatch batch = JdkRedisUtil.getRedissonClient().createBatch();

        // 存储所有token映射id
        for (Map.Entry<String, Long> entry : tokenMap.entrySet()) {
            batch.getBucket(entry.getKey()).setAsync(authorization.getId(), Duration.ofSeconds(entry.getValue()));
        }

        // 存储认证本体
        batch.getBucket(this.buildOAuth2Key(authorization.getId())).setAsync(authorization, Duration.ofSeconds(maxTimeOut));

        batch.execute();
    }

    @Override
    public void remove(@Nonnull OAuth2Authorization authorization) {
        List<String> keys = new ArrayList<>();
        // 移除认证本体
        keys.add(this.buildOAuth2Key(authorization.getId()));

        // 移除所有token
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        if (authorizationCode != null) {
            keys.add(this.buildOAuth2Key(authorizationCode.getToken().getTokenValue()));
        }

        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
        if (accessToken != null) {
            keys.add(this.buildOAuth2Key(accessToken.getToken().getTokenValue()));
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        if (refreshToken != null) {
            keys.add(this.buildOAuth2Key(refreshToken.getToken().getTokenValue()));
        }

        OAuth2Authorization.Token<OAuth2UserCode> userCodeToken = authorization.getToken(OAuth2UserCode.class);
        if (userCodeToken != null) {
            keys.add(this.buildOAuth2Key(userCodeToken.getToken().getTokenValue()));
        }

        OAuth2Authorization.Token<OAuth2DeviceCode> deviceCodeToken = authorization.getToken(OAuth2DeviceCode.class);
        if (deviceCodeToken != null) {
            keys.add(this.buildOAuth2Key(deviceCodeToken.getToken().getTokenValue()));
        }

        OAuth2Authorization.Token<OidcIdToken> oidcIdTokenToken = authorization.getToken(OidcIdToken.class);
        if (oidcIdTokenToken != null) {
            keys.add(this.buildOAuth2Key(oidcIdTokenToken.getToken().getTokenValue()));
        }

        JdkRedisUtil.getRedissonClient().getKeys().delete(keys.toArray(String[]::new));
    }

    @Override
    public OAuth2Authorization findById(String id) {
        throw new UnsupportedOperationException(this.getClass().getName() + "#findById is not support");
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        RedissonClient redissonClient = JdkRedisUtil.getRedissonClient();
        Object authorizeId = redissonClient.getBucket(this.buildOAuth2Key(token)).get();
        if (authorizeId == null) {
            return null;
        }

        return (OAuth2Authorization) redissonClient.getBucket(this.buildOAuth2Key(authorizeId.toString())).get();
    }

    // 构建OAuth2缓存KEY
    private String buildOAuth2Key(String key) {
        return String.format("%s:%s", OAUTH2_PREFIX, key);
    }
}
