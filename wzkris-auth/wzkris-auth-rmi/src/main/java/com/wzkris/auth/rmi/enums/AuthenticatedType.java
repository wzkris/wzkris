package com.wzkris.auth.rmi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 认证类型
 */
@Getter
@AllArgsConstructor
public enum AuthenticatedType {

    SYSTEM_USER("system_user"),

    CLIENT_USER("client_user"),

    CLIENT("client");

    private final String value;

}
