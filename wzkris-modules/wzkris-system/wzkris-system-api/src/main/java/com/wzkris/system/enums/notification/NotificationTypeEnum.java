package com.wzkris.system.enums.notification;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 通知类型
 */
@Getter
@AllArgsConstructor
public enum NotificationTypeEnum {

    SYSTEM("0", "系统通知"),
    DEVICE("1", "设备告警");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

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
}
