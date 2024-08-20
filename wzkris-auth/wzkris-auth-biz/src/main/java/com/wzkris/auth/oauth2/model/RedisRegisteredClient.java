package com.wzkris.auth.oauth2.model;

import lombok.Data;

import java.time.Instant;

/**
 * 基于redis存储的客户端实体
 *
 * @author wzkris
 */
@Data
public class RedisRegisteredClient {

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 客户端id签发时间
     */
    private Instant clientIdIssuedAt;

    /**
     * 客户端秘钥
     */
    private String clientSecret;

    /**
     * 客户端秘钥过期时间
     */
    private Instant clientSecretExpiresAt;

    /**
     * 客户端支持的认证方式
     */
    private String clientAuthenticationMethods;

    /**
     * 客户端支持的授权申请方式
     */
    private String authorizationGrantTypes;

    /**
     * 回调地址
     */
    private String redirectUris;

    /**
     * 登出回调地址
     */
    private String postLogoutRedirectUris;

    /**
     * 客户端拥有的scope
     */
    private String scopes;

    /**
     * 客户端配置
     */
    private String clientSettings;

    /**
     * 通过该客户端签发的access token设置
     */
    private String tokenSettings;

}