package com.wzkris.principal.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 性别
 */
@AllArgsConstructor
public enum GenderEnum {
    /**
     * 性别未知
     */
    UNKNOWN("2"),
    /**
     * 性别女
     */
    FEMALE("1"),
    /**
     * 性别男
     */
    MALE("0");

    private final String value;

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

    @JsonValue
    public String getValue() {
        return value;
    }

}
