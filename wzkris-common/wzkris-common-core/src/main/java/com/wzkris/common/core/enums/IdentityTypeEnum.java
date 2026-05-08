package com.wzkris.common.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 身份类型
 */
@Getter
@AllArgsConstructor
public enum IdentityTypeEnum {

    /**
     * 无身份
     */
    NONE("NONE"),

    /**
     * 超级身份
     */
    SUPER("SUPER");

    @JsonValue
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

}
