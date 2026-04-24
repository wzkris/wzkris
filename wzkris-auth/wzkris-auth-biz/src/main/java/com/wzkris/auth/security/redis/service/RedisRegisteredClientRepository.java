/*
 * Copyright 2020-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wzkris.auth.security.redis.service;

import com.wzkris.auth.properties.TokenProperties;
import com.wzkris.auth.remote.interfaces.common.request.StringValueRequest;
import com.wzkris.auth.remote.interfaces.oauth2.IOAuth2ClientRemote;
import com.wzkris.auth.remote.interfaces.oauth2.response.OAuth2ClientResponse;
import com.wzkris.auth.security.redis.entity.OAuth2RegisteredClient;
import com.wzkris.auth.security.redis.repository.OAuth2RegisteredClientRepository;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.I18nUtil;
import com.wzkris.common.core.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
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
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RedisRegisteredClientRepository implements RegisteredClientRepository {

    private final OAuth2RegisteredClientRepository registeredClientRepository;

    private final IOAuth2ClientRemote oAuth2ClientRemote;

    private final TokenProperties tokenProperties;

    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        OAuth2RegisteredClient oauth2RegisteredClient = ModelMapper.convertOAuth2RegisteredClient(registeredClient);
        this.registeredClientRepository.save(oauth2RegisteredClient);
    }

    @Nullable
    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");

        OAuth2RegisteredClient oAuth2RegisteredClient = this.registeredClientRepository.findById(id).orElse(null);
        if (oAuth2RegisteredClient != null) {
            return ModelMapper.convertRegisteredClient(oAuth2RegisteredClient);
        }

        Result<OAuth2ClientResponse> oauth2Client = oAuth2ClientRemote.queryById(new StringValueRequest(id));
        return checkAndSave(oauth2Client);
    }

    @Nullable
    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");

        OAuth2RegisteredClient oAuth2RegisteredClient = this.registeredClientRepository.findByClientId(clientId);

        if (oAuth2RegisteredClient != null) {
            return ModelMapper.convertRegisteredClient(oAuth2RegisteredClient);
        }

        Result<OAuth2ClientResponse> oauth2Client = oAuth2ClientRemote.queryByClientId(new StringValueRequest(clientId));
        return checkAndSave(oauth2Client);
    }

    private RegisteredClient checkAndSave(Result<OAuth2ClientResponse> oauth2ClientResult) {
        if (!ResultUtil.check(oauth2ClientResult)) {
            // 兼容org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationEndpointFilter#sendErrorResponse方法强转异常
            throw new OAuth2AuthorizationCodeRequestAuthenticationException(
                    new OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT, I18nUtil.message("oauth2.client.invalid"), null),
                    null);
        }
        OAuth2ClientResponse oauth2Client = oauth2ClientResult.getData();
        if (oauth2Client == null || !CommonConstants.STATUS_ENABLE.equals(oauth2Client.getStatus())) {
            // 兼容org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationEndpointFilter#sendErrorResponse方法强转异常
            throw new OAuth2AuthorizationCodeRequestAuthenticationException(
                    new OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT, I18nUtil.message("oauth2.client.invalid"), null),
                    null);
        }

        RegisteredClient registeredClient = this.buildRegisteredClient(oauth2Client);
        // 数据库存储放入缓存
        this.save(registeredClient);

        return registeredClient;
    }

    private RegisteredClient buildRegisteredClient(OAuth2ClientResponse oauth2Client) {
        RegisteredClient.Builder builder = RegisteredClient.withId(String.valueOf(oauth2Client.getId()))
                .clientId(oauth2Client.getClientId())
                .clientSecret(oauth2Client.getClientSecret())
                .clientAuthenticationMethods(clientAuthenticationMethods -> {
                    clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                    clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                    clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_JWT);
                    clientAuthenticationMethods.add(ClientAuthenticationMethod.NONE);
                })
                .authorizationGrantTypes(
                        authorizationGrantTypes -> { // 授权方式
                            for (String authorizedGrantType : oauth2Client.getAuthorizationGrantTypes()) {
                                authorizationGrantTypes.add(new AuthorizationGrantType(authorizedGrantType));
                            }
                        })
                .redirectUris(
                        redirectUris -> { // 回调地址
                            redirectUris.addAll(Arrays.asList(oauth2Client.getRedirectUris()));
                        })
                .scopes(
                        scopes -> { // scope
                            scopes.addAll(Arrays.asList(oauth2Client.getScopes()));
                        });

        builder.tokenSettings(TokenSettings.builder()
                        .authorizationCodeTimeToLive(Duration.ofSeconds(tokenProperties.getAuthorizationCodeTimeOut()))
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .accessTokenTimeToLive(Duration.ofSeconds(tokenProperties.getAccessTokenTimeOut()))
                        .refreshTokenTimeToLive(Duration.ofSeconds(tokenProperties.getRefreshTokenTimeOut()))
                        .reuseRefreshTokens(tokenProperties.getReuseRefreshTokens())
                        .deviceCodeTimeToLive(Duration.ofSeconds(tokenProperties.getDeviceCodeTimeOut()))
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(!oauth2Client.getAutoApprove())
                        .build());
        return builder.build();
    }

}

