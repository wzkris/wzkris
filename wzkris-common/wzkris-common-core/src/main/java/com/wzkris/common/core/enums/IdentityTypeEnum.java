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
     * 普通管理员
     */
    ADMIN_NORMAL("admin_normal"),

    /**
     * 超级管理员
     */
    ADMIN_SUPER("admin_super"),

    /**
     * 普通租户用户
     */
    TENANT_NORMAL("tenant_normal"),

    /**
     * 超级租户用户
     */
    TENANT_SUPER("tenant_super");

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

