package com.wzkris.auth.oauth2.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wzkris.auth.oauth2.model.RedisOAuth2Authorization;
import com.wzkris.auth.oauth2.utils.OAuth2JsonUtil;
import com.wzkris.common.redis.util.RedisUtil;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBatch;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Consumer;

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
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private static final String OAUTH2_PREFIX = "Authorization:info";

    private final RegisteredClientRepository registeredClientRepository;

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
        RBatch batch = RedisUtil.getClient().createBatch();

        // 存储所有token映射id
        for (Map.Entry<String, Long> entry : tokenMap.entrySet()) {
            batch.getBucket(entry.getKey()).setAsync(authorization.getId(), Duration.ofSeconds(entry.getValue()));
        }

        // 存储认证本体
        batch.getBucket(this.buildOAuth2Key(authorization.getId())).setAsync(this.toEntity(authorization), Duration.ofSeconds(maxTimeOut));

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

        RedisUtil.delObj(keys);
    }

    @Override
    public OAuth2Authorization findById(String id) {
        RedisOAuth2Authorization entity = RedisUtil.getObj(this.buildOAuth2Key(id));

        return this.toObject(entity);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        String tokenId = RedisUtil.getObj(this.buildOAuth2Key(token));
        if (tokenId == null) {
            return null;
        }

        return this.findById(tokenId);
    }

    // 构建OAuth2缓存KEY
    private String buildOAuth2Key(String key) {
        return String.format("%s:%s", OAUTH2_PREFIX, key);
    }

    /**
     * 将框架所需的类型转为redis中存储的类型
     *
     * @param authorization 框架所需的类型
     * @return redis中存储的类型
     */
    private RedisOAuth2Authorization toEntity(OAuth2Authorization authorization) {
        RedisOAuth2Authorization entity = new RedisOAuth2Authorization();
        entity.setId(authorization.getId());
        entity.setRegisteredClientId(authorization.getRegisteredClientId());
        entity.setPrincipalName(authorization.getPrincipalName());
        entity.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());
        entity.setAuthorizedScopes(StringUtils.collectionToCommaDelimitedString(authorization.getAuthorizedScopes()));
        entity.setAttributes(this.writeMap(authorization.getAttributes()));
        entity.setState(authorization.getAttribute(OAuth2ParameterNames.STATE));

        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                authorization.getToken(OAuth2AuthorizationCode.class);
        setTokenValues(
                authorizationCode,
                entity::setAuthorizationCodeValue,
                entity::setAuthorizationCodeIssuedAt,
                entity::setAuthorizationCodeExpiresAt,
                entity::setAuthorizationCodeMetadata
        );

        OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
                authorization.getToken(OAuth2AccessToken.class);
        setTokenValues(
                accessToken,
                entity::setAccessTokenValue,
                entity::setAccessTokenIssuedAt,
                entity::setAccessTokenExpiresAt,
                entity::setAccessTokenMetadata
        );
        if (accessToken != null && accessToken.getToken().getScopes() != null) {
            entity.setAccessTokenScopes(StringUtils.collectionToCommaDelimitedString(accessToken.getToken().getScopes()));
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
                authorization.getToken(OAuth2RefreshToken.class);
        setTokenValues(
                refreshToken,
                entity::setRefreshTokenValue,
                entity::setRefreshTokenIssuedAt,
                entity::setRefreshTokenExpiresAt,
                entity::setRefreshTokenMetadata
        );

        OAuth2Authorization.Token<OidcIdToken> oidcIdToken =
                authorization.getToken(OidcIdToken.class);
        setTokenValues(
                oidcIdToken,
                entity::setOidcIdTokenValue,
                entity::setOidcIdTokenIssuedAt,
                entity::setOidcIdTokenExpiresAt,
                entity::setOidcIdTokenMetadata
        );
        if (oidcIdToken != null) {
            entity.setOidcIdTokenClaims(this.writeMap(oidcIdToken.getClaims()));
        }

        OAuth2Authorization.Token<OAuth2UserCode> userCode =
                authorization.getToken(OAuth2UserCode.class);
        setTokenValues(
                userCode,
                entity::setUserCodeValue,
                entity::setUserCodeIssuedAt,
                entity::setUserCodeExpiresAt,
                entity::setUserCodeMetadata
        );

        OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode =
                authorization.getToken(OAuth2DeviceCode.class);
        setTokenValues(
                deviceCode,
                entity::setDeviceCodeValue,
                entity::setDeviceCodeIssuedAt,
                entity::setDeviceCodeExpiresAt,
                entity::setDeviceCodeMetadata
        );

        return entity;
    }

    /**
     * 将redis中存储的类型转为框架所需的类型
     *
     * @param entity redis中存储的类型
     * @return 框架所需的类型
     */
    private OAuth2Authorization toObject(RedisOAuth2Authorization entity) {
        RegisteredClient registeredClient = this.registeredClientRepository.findById(entity.getRegisteredClientId());
        if (registeredClient == null) {
            throw new DataRetrievalFailureException(
                    "The RegisteredClient with id '" + entity.getRegisteredClientId() + "' was not found in the RegisteredClientRepository.");
        }

        OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .id(entity.getId())
                .principalName(entity.getPrincipalName())
                .authorizationGrantType(new AuthorizationGrantType(entity.getAuthorizationGrantType()))
                .authorizedScopes(StringUtils.commaDelimitedListToSet(entity.getAuthorizedScopes()))
                .attributes(attributes -> attributes.putAll(this.parseMap(entity.getAttributes())));
        if (entity.getState() != null) {
            builder.attribute(OAuth2ParameterNames.STATE, entity.getState());
        }

        if (entity.getAuthorizationCodeValue() != null) {
            OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(
                    entity.getAuthorizationCodeValue(),
                    entity.getAuthorizationCodeIssuedAt(),
                    entity.getAuthorizationCodeExpiresAt());
            builder.token(authorizationCode, metadata -> metadata.putAll(this.parseMap(entity.getAuthorizationCodeMetadata())));
        }

        if (entity.getAccessTokenValue() != null) {
            OAuth2AccessToken accessToken = new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    entity.getAccessTokenValue(),
                    entity.getAccessTokenIssuedAt(),
                    entity.getAccessTokenExpiresAt(),
                    StringUtils.commaDelimitedListToSet(entity.getAccessTokenScopes()));
            builder.token(accessToken, metadata -> metadata.putAll(this.parseMap(entity.getAccessTokenMetadata())));
        }

        if (entity.getRefreshTokenValue() != null) {
            OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
                    entity.getRefreshTokenValue(),
                    entity.getRefreshTokenIssuedAt(),
                    entity.getRefreshTokenExpiresAt());
            builder.token(refreshToken, metadata -> metadata.putAll(this.parseMap(entity.getRefreshTokenMetadata())));
        }

        if (entity.getOidcIdTokenValue() != null) {
            OidcIdToken idToken = new OidcIdToken(
                    entity.getOidcIdTokenValue(),
                    entity.getOidcIdTokenIssuedAt(),
                    entity.getOidcIdTokenExpiresAt(),
                    this.parseMap(entity.getOidcIdTokenClaims()));
            builder.token(idToken, metadata -> metadata.putAll(this.parseMap(entity.getOidcIdTokenMetadata())));
        }

        if (entity.getUserCodeValue() != null) {
            OAuth2UserCode userCode = new OAuth2UserCode(
                    entity.getUserCodeValue(),
                    entity.getUserCodeIssuedAt(),
                    entity.getUserCodeExpiresAt());
            builder.token(userCode, metadata -> metadata.putAll(this.parseMap(entity.getUserCodeMetadata())));
        }

        if (entity.getDeviceCodeValue() != null) {
            OAuth2DeviceCode deviceCode = new OAuth2DeviceCode(
                    entity.getDeviceCodeValue(),
                    entity.getDeviceCodeIssuedAt(),
                    entity.getDeviceCodeExpiresAt());
            builder.token(deviceCode, metadata -> metadata.putAll(this.parseMap(entity.getDeviceCodeMetadata())));
        }

        return builder.build();
    }

    /**
     * 设置token的值
     *
     * @param token              Token实例
     * @param tokenValueConsumer set方法
     * @param issuedAtConsumer   set方法
     * @param expiresAtConsumer  set方法
     * @param metadataConsumer   set方法
     */
    private void setTokenValues(
            OAuth2Authorization.Token<?> token,
            Consumer<String> tokenValueConsumer,
            Consumer<Instant> issuedAtConsumer,
            Consumer<Instant> expiresAtConsumer,
            Consumer<String> metadataConsumer) {
        if (token != null) {
            OAuth2Token oAuth2Token = token.getToken();
            tokenValueConsumer.accept(oAuth2Token.getTokenValue());
            issuedAtConsumer.accept(oAuth2Token.getIssuedAt());
            expiresAtConsumer.accept(oAuth2Token.getExpiresAt());
            metadataConsumer.accept(this.writeMap(token.getMetadata()));
        }
    }

    /**
     * 将json转为map
     *
     * @param data json
     * @return map对象
     */
    private Map<String, Object> parseMap(String data) {
        try {
            return OAuth2JsonUtil.getObjectMapper().readValue(data, new TypeReference<>() {
            });
        }
        catch (Exception ex) {
            log.info("Exception json：{}", data);
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    /**
     * 将map对象转为json字符串
     *
     * @param metadata map对象
     * @return json字符串
     */
    private String writeMap(Map<String, Object> metadata) {
        try {
            return OAuth2JsonUtil.getObjectMapper().writeValueAsString(metadata);
        }
        catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }
}
