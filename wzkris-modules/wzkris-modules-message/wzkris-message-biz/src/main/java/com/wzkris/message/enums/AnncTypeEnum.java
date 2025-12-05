package com.wzkris.message.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 公告类型
 */
@AllArgsConstructor
public enum AnncTypeEnum {
    // 系统公告
    SYSTEM("1"),
    // APP公告
    APP("2");

    private final String value;

    @JsonCreator
    @Nullable
    public static AnncTypeEnum fromValue(String value) {
        for (AnncTypeEnum typeEnum : values()) {
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
