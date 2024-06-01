package com.thingslink.auth.oauth2.service;

import com.thingslink.auth.config.TokenConfig;
import com.thingslink.auth.domain.OAuth2Client;
import com.thingslink.auth.mapper.OAuth2ClientMapper;
import com.thingslink.common.core.utils.MessageUtil;
import com.thingslink.common.redis.util.RedisUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
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

    private final OAuth2ClientMapper oauth2ClientMapper;

    @Override
    public void save(RegisteredClient registeredClient) {
        throw new UnsupportedOperationException("You have to register app");
    }

    @Override
    public RegisteredClient findById(String id) {
        return this.findByClientId(id);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        OAuth2Client oauth2Client = RedisUtil.getObj(this.buildRedisKey(clientId));

        if (oauth2Client == null) {
            oauth2Client = oauth2ClientMapper.selectByClientId(clientId);

            if (oauth2Client == null || !"0".equals(oauth2Client.getStatus())) {
                // 兼容org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationEndpointFilter#sendErrorResponse方法强转异常
                throw new OAuth2AuthorizationCodeRequestAuthenticationException(
                        new OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT, MessageUtil.message("oauth2.client.invalid"), null), null);
            }

            // 数据库存储放入缓存
            RedisUtil.setObj(this.buildRedisKey(clientId), oauth2Client, tokenConfig.getRefreshTokenTimeOut());
        }

        return this.buildRegisteredClient(oauth2Client);
    }

    /**
     * 构建Spring OAuth2所需要的客户端信息
     */
    private RegisteredClient buildRegisteredClient(OAuth2Client oauth2Client) {
        RegisteredClient.Builder builder = RegisteredClient.withId(oauth2Client.getId().toString())
                .clientId(oauth2Client.getClientId())
                .clientSecret(oauth2Client.getClientSecret())
                .clientAuthenticationMethods(clientAuthenticationMethods -> {
                    clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                    clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                })
                .authorizationGrantTypes(authorizationGrantTypes -> {// 授权方式
                    for (String authorizedGrantType : oauth2Client.getAuthorizationGrantTypes()) {
                        authorizationGrantTypes.add(new AuthorizationGrantType(authorizedGrantType));
                    }
                })
                .redirectUris(redirectUris -> {// 回调地址
                    redirectUris.addAll(oauth2Client.getRedirectUris());
                })
                .scopes(scopes -> {// scope
                    scopes.addAll(oauth2Client.getScopes());
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
                        .requireAuthorizationConsent(!oauth2Client.getAutoApprove())
                        .build());
        return builder.build();
    }

    // 构建客户端缓存KEY
    private String buildRedisKey(String clientId) {
        return String.format("%s:%s", PREFIX, clientId);
    }
}
