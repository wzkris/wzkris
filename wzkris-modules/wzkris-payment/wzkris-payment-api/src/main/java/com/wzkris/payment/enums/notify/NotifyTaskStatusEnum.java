package com.wzkris.payment.enums.notify;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 业务方通知任务状态
 *
 * @author wzkris
 */
@Getter
@AllArgsConstructor
public enum NotifyTaskStatusEnum {
    PENDING("PENDING", "待通知"),
    SENDING("SENDING", "通知中"),
    SUCCESS("SUCCESS", "通知成功"),
    FAILED("FAILED", "通知失败");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

    @JsonCreator
    @Nullable
    public static NotifyTaskStatusEnum fromValue(String value) {
        for (NotifyTaskStatusEnum status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}
