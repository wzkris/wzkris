package com.wzkris.auth.oauth2.model;

import lombok.Data;

/**
 * 基于redis的授权确认存储实体
 *
 * @author vains
 */
@Data
public class RedisAuthorizationConsent {

    /**
     * 额外提供的主键
     */
    private String id;

    /**
     * 当前授权确认的客户端id
     */
    private String registeredClientId;

    /**
     * 当前授权确认用户的 username
     */
    private String principalName;

    /**
     * 授权确认的scope
     */
    private String authorities;

}