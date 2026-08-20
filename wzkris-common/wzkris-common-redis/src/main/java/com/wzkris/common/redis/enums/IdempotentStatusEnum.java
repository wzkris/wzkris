package com.wzkris.common.redis.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;

/**
 * 幂等处理状态
 *
 * @author wzkris
 */
@AllArgsConstructor
public enum IdempotentStatusEnum {
    PROCESSING("processing"),
    DONE("done");

    @JsonValue
    private final String value;

    @Nullable
    @JsonCreator
    public static IdempotentStatusEnum fromValue(String value) {
        for (IdempotentStatusEnum typeEnum : values()) {
            if (typeEnum.value.equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
