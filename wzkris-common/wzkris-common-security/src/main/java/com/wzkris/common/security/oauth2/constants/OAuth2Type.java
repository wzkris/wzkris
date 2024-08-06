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
    SYS_USER("sys_user"),
    APP_USER("app_user"),
    CLIENT("client");

    private final String value;
}
