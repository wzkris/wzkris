package com.thingslink.common.security.oauth2.constants;

import lombok.Getter;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : OAuth2类型
 * @date : 2024/5/16 13:08
 */
@Getter
public enum OAuth2Type {
    SYS_USER("sys_user"),
    APP_USER("app_user"),
    CLIENT("client");

    private final String value;

    OAuth2Type(String value) {
        this.value = value;
    }

}
