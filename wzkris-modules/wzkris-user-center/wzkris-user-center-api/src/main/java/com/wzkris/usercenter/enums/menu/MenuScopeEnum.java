package com.wzkris.usercenter.enums.menu;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 菜单类型
 */
@Getter
@AllArgsConstructor
public enum MenuScopeEnum {
    SYSTEM("system", "系统域"),
    TENANT("tenant", "租户域");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

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

}
