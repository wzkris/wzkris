package com.thingslink.auth.oauth2.service;

import com.thingslink.auth.config.TokenConfig;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.redis.util.RedisUtil;
import com.thingslink.common.security.utils.OAuth2EndpointUtil;
import com.thingslink.user.api.RemoteOAuth2ClientApi;
import com.thingslink.user.api.domain.dto.Oauth2ClientDTO;
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

    private final RemoteOAuth2ClientApi remoteOAuth2ClientApi;

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
        Oauth2ClientDTO oauth2ClientDTO = RedisUtil.getCacheObject(this.buildRedisKey(clientId));

        if (oauth2ClientDTO == null) {
            Result<Oauth2ClientDTO> clientResult = remoteOAuth2ClientApi.getByClientId(clientId);
            oauth2ClientDTO = clientResult.checkData();

            if (oauth2ClientDTO == null) {
                OAuth2EndpointUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_CLIENT, "oauth2.client.invalid");
            }

            if (!Oauth2ClientDTO.Status.NORMAL.value().equals(oauth2ClientDTO.getStatus())) {
                OAuth2EndpointUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_CLIENT, "oauth2.client.invalid");
            }

            // 数据库存储放入缓存
            RedisUtil.setCacheObject(this.buildRedisKey(clientId), oauth2ClientDTO, Duration.ofSeconds(tokenConfig.getRefreshTokenTimeOut()));
        }

        return this.buildRegisteredClient(oauth2ClientDTO);
    }

    /**
     * 构建Spring OAuth2所需要的客户端信息
     */
    private RegisteredClient buildRegisteredClient(Oauth2ClientDTO oauth2ClientDTO) {
        RegisteredClient.Builder builder = RegisteredClient.withId(oauth2ClientDTO.getId().toString())
                .clientId(oauth2ClientDTO.getClientId())
                .clientSecret(oauth2ClientDTO.getClientSecret())
                .clientAuthenticationMethods(clientAuthenticationMethods -> {
                    clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                    clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                })
                .authorizationGrantTypes(authorizationGrantTypes -> {// 授权方式
                    for (String authorizedGrantType : oauth2ClientDTO.getAuthorizationGrantTypes()) {
                        authorizationGrantTypes.add(new AuthorizationGrantType(authorizedGrantType));
                    }
                })
                .redirectUris(redirectUris -> {// 回调地址
                    redirectUris.addAll(oauth2ClientDTO.getRedirectUris());
                })
                .scopes(scopes -> {// scope
                    scopes.addAll(oauth2ClientDTO.getScopes());
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
                        .requireAuthorizationConsent(!oauth2ClientDTO.getAutoApprove())
                        .build());
        return builder.build();
    }

    // 构建客户端缓存KEY
    private String buildRedisKey(String clientId) {
        return String.format("%s:%s", PREFIX, clientId);
    }
}
