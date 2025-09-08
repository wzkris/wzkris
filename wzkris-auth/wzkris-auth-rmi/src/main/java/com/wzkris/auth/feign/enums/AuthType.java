package com.wzkris.auth.feign.enums;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 认证类型
 */
@Getter
@AllArgsConstructor
public enum AuthType {

    USER("user"),

    CUSTOMER("customer"),

    CLIENT("oauth2_client");

    private final String value;

    @Nullable
    public static AuthType fromValue(String value) {
        for (AuthType authType : values()) {
            if (authType.value.equals(value)) {
                return authType;
            }
        }
        return null;
    }

}
