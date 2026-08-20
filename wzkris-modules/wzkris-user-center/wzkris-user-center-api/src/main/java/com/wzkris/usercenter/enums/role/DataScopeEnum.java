package com.wzkris.usercenter.enums.role;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public enum DataScopeEnum {

    ALL("1", "所有数据权限"),
    CUSTOM("2", "自定义数据权限"),
    DEPT("3", "本部门数据权限"),
    DEPT_AND_CHILD("4", "本部门及以下数据权限"),
    ONLY_SELF("5", "仅本人数据权限");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

    @JsonCreator
    @Nullable
    public static DataScopeEnum fromValue(String value) {
        for (DataScopeEnum statusEnum : values()) {
            if (statusEnum.value.equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

}
