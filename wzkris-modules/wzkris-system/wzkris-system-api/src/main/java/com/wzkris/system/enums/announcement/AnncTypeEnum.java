package com.wzkris.system.enums.announcement;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 公告类型
 */
@Getter
@AllArgsConstructor
public enum AnncTypeEnum {
    SYSTEM("1", "系统公告"),
    APP("2", "APP公告");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

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
}
