package com.wzkris.system.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 公告状态
 */
@AllArgsConstructor
public enum AnncStatusEnum {
    // 草稿
    DRAFT("0"),
    // 关闭
    CLOSED("1"),
    // 已发布
    PUBLISH("2");

    private final String value;

    @JsonCreator
    @Nullable
    public static AnncStatusEnum fromValue(String value) {
        for (AnncStatusEnum typeEnum : values()) {
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
