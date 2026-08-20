package com.wzkris.usercenter.enums.user;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 性别
 */
@Getter
@AllArgsConstructor
public enum GenderEnum {
    /**
     * 性别未知
     */
    UNKNOWN("2", "性别未知"),
    /**
     * 性别女
     */
    FEMALE("1", "性别女"),
    /**
     * 性别男
     */
    MALE("0", "性别男");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

    @JsonCreator
    @Nullable
    public static GenderEnum fromValue(String value) {
        for (GenderEnum genderEnum : values()) {
            if (genderEnum.value.equals(value)) {
                return genderEnum;
            }
        }
        return null;
    }

}
