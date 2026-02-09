package com.wzkris.common.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 身份类型
 */
@AllArgsConstructor
public enum IdentityTypeEnum {

    /**
     * 普通身份
     */
    NORMAL("NORMAL"),

    /**
     * 超级身份
     */
    SUPER("SUPER");

    private final String value;

    @JsonCreator
    @Nullable
    public static IdentityTypeEnum fromValue(String value) {
        for (IdentityTypeEnum typeEnum : values()) {
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

