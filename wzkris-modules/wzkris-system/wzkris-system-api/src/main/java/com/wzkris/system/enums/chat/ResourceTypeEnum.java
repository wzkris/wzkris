package com.wzkris.system.enums.chat;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public enum ResourceTypeEnum {

    TEXT("text"),
    IMAGE("image"),
    VIDEO("video"),
    FILE("file");

    @EnumValue
    @JsonValue
    private final String value;

    @JsonCreator
    @Nullable
    public static ResourceTypeEnum fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (ResourceTypeEnum e : values()) {
            if (e.value.equalsIgnoreCase(value)) {
                return e;
            }
        }
        return null;
    }
}
