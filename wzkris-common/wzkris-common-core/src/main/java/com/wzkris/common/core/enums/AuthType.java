package com.wzkris.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

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
