package com.wzkris.common.security.oauth2.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : OAuth2类型
 * @date : 2024/5/16 13:08
 */
@Getter
@AllArgsConstructor
public enum OAuth2Type {
    /**
     * 系统用户
     */
    SYS_USER("sys_user"),
    /**
     * APP用户
     */
    APP_USER("app_user"),
    /**
     * 客户端
     */
    CLIENT("client");

    private final String value;
}
