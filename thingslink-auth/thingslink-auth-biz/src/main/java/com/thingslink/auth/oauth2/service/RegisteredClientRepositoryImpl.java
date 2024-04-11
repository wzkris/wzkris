package com.thingslink.auth.oauth2.service;

import com.thingslink.auth.domain.Oauth2RegisteredClient;
import com.thingslink.auth.mapper.Oauth2RegisteredClientMapper;
import com.thingslink.common.redis.util.RedisUtil;
import com.thingslink.common.security.config.TokenConfig;
import com.thingslink.common.security.utils.OAuth2EndpointUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @date : 2024/2/23 10:03
 * @description : 客户端持久化到数据库
 */
@Service
@AllArgsConstructor
public class RegisteredClientRepositoryImpl implements RegisteredClientRepository {

    private static final String PREFIX = "Authorization:client";

    private final TokenConfig tokenConfig;

    private final Oauth2RegisteredClientMapper oauth2RegisteredClientMapper;

    @Override
    public void save(RegisteredClient registeredClient) {
        throw new UnsupportedOperationException("You have to register app");
    }

    @Override
    public RegisteredClient findById(String id) {
        throw new UnsupportedOperationException(this.getClass().getName() + "#findById is not support");
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Oauth2RegisteredClient oauth2RegisteredClient = RedisUtil.getCacheObject(this.buildRedisKey(clientId));

        if (oauth2RegisteredClient == null) {
            oauth2RegisteredClient = oauth2RegisteredClientMapper.selectByClientId(clientId);

            if (oauth2RegisteredClient == null) {
                OAuth2EndpointUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_CLIENT, "oauth2.client.invalid");
            }

            if (!Oauth2RegisteredClient.Status.NORMAL.value().equals(oauth2RegisteredClient.getStatus())) {
                OAuth2EndpointUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_CLIENT, "oauth2.client.invalid");
            }

            // 数据库存储放入缓存
            RedisUtil.setCacheObject(this.buildRedisKey(clientId), oauth2RegisteredClient, tokenConfig.getRefreshTokenTimeOut());
        }

        return this.buildRegisteredClient(oauth2RegisteredClient);
    }

    /**
     * 构建Spring OAuth2所需要的客户端信息
     */
    private RegisteredClient buildRegisteredClient(Oauth2RegisteredClient oauth2RegisteredClient) {
        RegisteredClient.Builder builder = RegisteredClient.withId(oauth2RegisteredClient.getId().toString())
                .clientId(oauth2RegisteredClient.getClientId())
                .clientSecret(oauth2RegisteredClient.getClientSecret())
                .clientAuthenticationMethods(clientAuthenticationMethods -> {
                    clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                    clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                })
                .authorizationGrantTypes(authorizationGrantTypes -> {// 授权方式
                    for (String authorizedGrantType : oauth2RegisteredClient.getAuthorizationGrantTypes()) {
                        authorizationGrantTypes.add(new AuthorizationGrantType(authorizedGrantType));
                    }
                })
                .redirectUris(redirectUris -> {// 回调地址
                    redirectUris.addAll(oauth2RegisteredClient.getRedirectUris());
                })
                .scopes(scopes -> {// scope
                    scopes.addAll(oauth2RegisteredClient.getScopes());
                });

        builder.tokenSettings(TokenSettings.builder()
                        .authorizationCodeTimeToLive(Duration.ofSeconds(tokenConfig.getAuthorizationCodeTimeOut()))
                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE) // 使用匿名token
                        .accessTokenTimeToLive(Duration.ofSeconds(tokenConfig.getAccessTokenTimeOut()))
                        .refreshTokenTimeToLive(Duration.ofSeconds(tokenConfig.getRefreshTokenTimeOut()))
                        .reuseRefreshTokens(true)// 复用refresh_token
                        .deviceCodeTimeToLive(Duration.ofSeconds(tokenConfig.getDeviceCodeTimeOut()))
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(!oauth2RegisteredClient.getAutoApprove())
                        .build());
        return builder.build();
    }

    // 构建客户端缓存KEY
    private String buildRedisKey(String clientId) {
        return String.format("%s:%s", PREFIX, clientId);
    }
}
