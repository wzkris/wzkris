package com.wzkris.common.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 认证类型
 */
@Getter
@AllArgsConstructor
public enum AuthType {

    ANONYMOUS("anonymous"),

    ADMIN("admin"),

    TENANT("tenant"),

    CUSTOMER("customer"),

    CLIENT("oauth2_client");

    private final String value;

    @JsonCreator
    @Nullable
    public static AuthType fromValue(String value) {
        for (AuthType authType : values()) {
            if (authType.value.equals(value)) {
                return authType;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
