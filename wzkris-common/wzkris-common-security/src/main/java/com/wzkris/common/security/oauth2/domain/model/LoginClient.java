package com.wzkris.common.security.oauth2.domain.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 登录客户端
 * @date : 2024/5/16 15:36
 */
@Data
public class LoginClient implements Serializable {
    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 客户端名称
     */
    private String clientName;
}
