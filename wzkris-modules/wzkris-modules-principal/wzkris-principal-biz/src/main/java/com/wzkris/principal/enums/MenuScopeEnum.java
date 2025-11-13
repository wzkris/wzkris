package com.wzkris.principal.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 菜单类型
 */
@AllArgsConstructor
public enum MenuScopeEnum {
    // 系统域
    SYSTEM("system"),
    // 租户域
    TENANT("tenant");

    private final String value;

    @JsonCreator
    @Nullable
    public static MenuScopeEnum fromValue(String value) {
        for (MenuScopeEnum scopeEnum : values()) {
            if (scopeEnum.value.equals(value)) {
                return scopeEnum;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
