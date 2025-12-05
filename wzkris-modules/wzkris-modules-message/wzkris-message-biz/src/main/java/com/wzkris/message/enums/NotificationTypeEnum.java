package com.wzkris.message.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 通知类型
 */
@AllArgsConstructor
public enum NotificationTypeEnum {

    // 系统通知
    SYSTEM("0"),
    // 设备告警
    DEVICE("1");

    private final String value;

    @JsonCreator
    @Nullable
    public static NotificationTypeEnum fromValue(String value) {
        for (NotificationTypeEnum typeEnum : values()) {
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
