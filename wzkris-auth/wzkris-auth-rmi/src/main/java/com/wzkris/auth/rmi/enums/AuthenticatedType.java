package com.wzkris.auth.rmi.enums;

import jakarta.annotation.Nullable;
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

    @Nullable
    public static AuthenticatedType fromValue(String value) {
        for (AuthenticatedType authenticatedType : values()) {
            if (authenticatedType.value.equals(value)) {
                return authenticatedType;
            }
        }
        return null;
    }

}
