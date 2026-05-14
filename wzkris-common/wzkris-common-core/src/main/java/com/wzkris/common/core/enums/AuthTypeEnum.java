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

    ADMIN("ADMIN", "管理员"),

    TENANT("TENANT", "租户商家"),

    CUSTOMER("CUSTOMER", "用户"),

    CLIENT("CLIENT", "客户端"),

    NONE("NONE", "无");

    @JsonValue
    private final String value;

    private final String description;

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
