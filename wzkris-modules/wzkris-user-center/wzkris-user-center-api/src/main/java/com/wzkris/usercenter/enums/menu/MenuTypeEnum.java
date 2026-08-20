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
public enum MenuTypeEnum {
    DIR("D", "目录"),
    MENU("M", "菜单"),
    BUTTON("B", "按钮"),
    INNERLINK("I", "内链"),
    OUTLINK("O", "外链");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

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

}
