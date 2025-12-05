package com.wzkris.principal.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 菜单类型
 */
@AllArgsConstructor
public enum MenuTypeEnum {
    //（目录）
    DIR("D"),
    //（菜单）
    MENU("M"),
    //（按钮）
    BUTTON("B"),
    //（内链）
    INNERLINK("I"),
    // （外链）
    OUTLINK("O");

    private final String value;

    @JsonCreator
    @Nullable
    public static MenuTypeEnum fromValue(String value) {
        for (MenuTypeEnum typeEnum : values()) {
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
