package com.thingslink.auth.oauth2.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @date : 2024/2/23 10:03
 * @description : OAuth2AuthorizationConsentService 是一个中心组件，新的授权同意被存储，现有的授权同意被查询。
 * 它主要被实现OAuth2授权请求流程的组件使用，例如：authorization_code grant
 */
@Service
@AllArgsConstructor
public class OAuth2AuthorizationConsentServiceImpl implements OAuth2AuthorizationConsentService {

    private final static Long DEFAULT_TIMEOUT = 180L;

    private static final String PREFIX = "Authorization:consent";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        redisTemplate.opsForValue().set(this.buildRedisKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName()),
                authorizationConsent, DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        redisTemplate.delete(this.buildRedisKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName()));
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        return (OAuth2AuthorizationConsent) redisTemplate.opsForValue().get(this.buildRedisKey(registeredClientId, principalName));
    }

    // 构建客户端缓存KEY
    private String buildRedisKey(String clientId, String principalName) {
        return String.format("%s:%s:%s", PREFIX, clientId, principalName);
    }
}
