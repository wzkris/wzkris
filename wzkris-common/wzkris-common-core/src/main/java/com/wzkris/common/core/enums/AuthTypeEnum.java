package com.wzkris.common.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 认证类型
 */
@AllArgsConstructor
public enum AuthTypeEnum {

    ANONYMOUS("anonymous"),

    ADMIN("admin"),

    TENANT("tenant"),

    CUSTOMER("customer"),

    CLIENT("oauth2_client");

    private final String value;

    @JsonCreator
    @Nullable
    public static AuthTypeEnum fromValue(String value) {
        for (AuthTypeEnum typeEnum : values()) {
            if (typeEnum.value.equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
