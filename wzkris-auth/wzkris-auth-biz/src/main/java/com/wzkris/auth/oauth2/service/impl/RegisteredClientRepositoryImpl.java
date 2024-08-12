package com.wzkris.auth.oauth2.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wzkris.auth.config.TokenConfig;
import com.wzkris.auth.oauth2.model.RedisRegisteredClient;
import com.wzkris.auth.oauth2.redis.OAuth2JsonUtil;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.MessageUtil;
import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.common.security.oauth2.constants.OAuth2SecurityConstants;
import com.wzkris.user.api.RemoteOAuth2ClientApi;
import com.wzkris.user.api.domain.dto.OAuth2ClientDTO;
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
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;
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
        RedisRegisteredClient entity = this.toEntity(registeredClient);
        RedisUtil.setObj(this.buildRedisKey(entity.getClientId()), entity, tokenConfig.getRefreshTokenTimeOut());
    }

    @Override
    public RegisteredClient findById(String id) {
        return this.findByClientId(id);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        RedisRegisteredClient redisRegisteredClient = RedisUtil.getObj(this.buildRedisKey(clientId));

        if (redisRegisteredClient != null) {
            return this.toObject(redisRegisteredClient);
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
        this.save(registeredClient);

        return registeredClient;
    }

    // 构建客户端缓存KEY
    private String buildRedisKey(String clientId) {
        return String.format("%s:%s", PREFIX, clientId);
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

    private RegisteredClient toObject(RedisRegisteredClient client) {
        RegisteredClient.Builder builder = RegisteredClient.withId(client.getClientId())
                .clientId(client.getClientId())
                .clientIdIssuedAt(client.getClientIdIssuedAt())
                .clientSecret(client.getClientSecret())
                .clientSecretExpiresAt(client.getClientSecretExpiresAt())
                .clientAuthenticationMethods(authenticationMethods -> {
                    for (String authenticationMethod : StringUtils.commaDelimitedListToSet(client.getClientAuthenticationMethods())) {
                        authenticationMethods.add(new ClientAuthenticationMethod(authenticationMethod));
                    }
                })
                .authorizationGrantTypes(grantTypes -> {
                    for (String grantType : StringUtils.commaDelimitedListToSet(client.getAuthorizationGrantTypes())) {
                        grantTypes.add(new AuthorizationGrantType(grantType));
                    }
                })
                .redirectUris(uris -> uris.addAll(StringUtils.commaDelimitedListToSet(client.getRedirectUris())))
                .postLogoutRedirectUris(uris -> uris.addAll(StringUtils.commaDelimitedListToSet(client.getPostLogoutRedirectUris())))
                .scopes(scopes -> scopes.addAll(StringUtils.commaDelimitedListToSet(client.getScopes())));

        Map<String, Object> clientSettingsMap = this.parseMap(client.getClientSettings());
        builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());

        Map<String, Object> tokenSettingsMap = this.parseMap(client.getTokenSettings());
        builder.tokenSettings(TokenSettings.withSettings(tokenSettingsMap).build());

        return builder.build();
    }

    private RedisRegisteredClient toEntity(RegisteredClient registeredClient) {
        List<String> clientAuthenticationMethods = registeredClient.getClientAuthenticationMethods().stream()
                .map(ClientAuthenticationMethod::getValue).toList();

        List<String> authorizationGrantTypes = registeredClient.getAuthorizationGrantTypes().stream()
                .map(AuthorizationGrantType::getValue).collect(Collectors.toList());

        RedisRegisteredClient entity = new RedisRegisteredClient();
        entity.setClientId(registeredClient.getClientId());
        entity.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
        entity.setClientSecret(registeredClient.getClientSecret());
        entity.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
        entity.setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(clientAuthenticationMethods));
        entity.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes));
        entity.setRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
        entity.setPostLogoutRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getPostLogoutRedirectUris()));
        entity.setScopes(StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()));
        entity.setClientSettings(this.writeMap(registeredClient.getClientSettings().getSettings()));
        entity.setTokenSettings(this.writeMap(registeredClient.getTokenSettings().getSettings()));

        return entity;
    }

    private Map<String, Object> parseMap(String data) {
        try {
            return OAuth2JsonUtil.getObjectMapper().readValue(data, new TypeReference<>() {
            });
        }
        catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    private String writeMap(Map<String, Object> data) {
        try {
            return OAuth2JsonUtil.getObjectMapper().writeValueAsString(data);
        }
        catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

}
