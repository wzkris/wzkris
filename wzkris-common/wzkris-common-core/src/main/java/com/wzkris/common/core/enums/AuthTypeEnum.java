package com.wzkris.common.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 认证类型
 */
@Getter
@AllArgsConstructor
public enum AuthTypeEnum {

    ADMIN("admin"),

    TENANT("tenant"),

    CUSTOMER("customer"),

    CLIENT("client"),

    NONE("none");

    @JsonValue
    private final String value;

    @JsonCreator
    public static AuthTypeEnum fromValue(String value) {
        for (AuthTypeEnum typeEnum : values()) {
            if (typeEnum.value.equals(value)) {
                return typeEnum;
            }
        }
        return NONE;
    }

}
