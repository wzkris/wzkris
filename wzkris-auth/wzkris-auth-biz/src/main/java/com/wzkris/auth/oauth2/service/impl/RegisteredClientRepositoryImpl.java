package com.wzkris.auth.oauth2.service.impl;

import com.wzkris.auth.config.TokenConfig;
import com.wzkris.auth.oauth2.redis.JdkRedisUtil;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.MessageUtil;
import com.wzkris.common.security.oauth2.constants.OAuth2SecurityConstants;
import com.wzkris.user.api.RemoteOAuth2ClientApi;
import com.wzkris.user.api.domain.dto.OAuth2ClientDTO;
import lombok.AllArgsConstructor;
import org.redisson.api.RBucket;
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
import java.util.stream.Collectors;

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
        return this.findByClientId(id);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        RBucket<RegisteredClient> bucket = JdkRedisUtil.getRedissonClient().getBucket(this.buildRedisKey(clientId));
        if (bucket.get() != null) {
            return bucket.get();
        }

        Result<OAuth2ClientDTO> clientDTOResult = remoteOAuth2ClientApi.getByClientId(clientId);
        OAuth2ClientDTO oauth2Client = clientDTOResult.checkData();

        if (oauth2Client == null || !CommonConstants.STATUS_ENABLE.equals(oauth2Client.getStatus())) {
            // 兼容org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationEndpointFilter#sendErrorResponse方法强转异常
            throw new OAuth2AuthorizationCodeRequestAuthenticationException(
                    new OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT, MessageUtil.message("oauth2.client.invalid"), null), null);
        }

        RegisteredClient registeredClient = this.buildRegisteredClient(oauth2Client);
        // 数据库存储放入缓存
        bucket.set(registeredClient, Duration.ofSeconds(tokenConfig.getRefreshTokenTimeOut()));
        return registeredClient;
    }

    /**
     * 构建Spring OAuth2所需要的客户端信息
     */
    private RegisteredClient buildRegisteredClient(OAuth2ClientDTO oauth2Client) {
        RegisteredClient.Builder builder = RegisteredClient.withId(oauth2Client.getClientId())
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
                    scopes.addAll(oauth2Client.getScopes()
                            .stream().map(scope -> OAuth2SecurityConstants.SCOPE_PREFIX + scope)
                            .collect(Collectors.toSet())
                    );
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
