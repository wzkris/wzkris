package com.wzkris.system.enums.announcement;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 公告状态
 */
@Getter
@AllArgsConstructor
public enum AnncStatusEnum {
    DRAFT("0", "草稿"),
    CLOSED("1", "关闭"),
    PUBLISH("2", "已发布");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

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
}
