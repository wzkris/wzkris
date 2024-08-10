package com.wzkris.auth.oauth2.service.impl;

import com.wzkris.common.redis.util.RedisUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Service;

import java.time.Duration;

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

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        RedisUtil.setObj(this.buildRedisKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName()),
                authorizationConsent, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        RedisUtil.delObj(this.buildRedisKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName()));
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        return RedisUtil.getObj(this.buildRedisKey(registeredClientId, principalName));
    }

    // 构建客户端缓存KEY
    private String buildRedisKey(String clientId, String principalName) {
        return String.format("%s:%s:%s", PREFIX, clientId, principalName);
    }
}
