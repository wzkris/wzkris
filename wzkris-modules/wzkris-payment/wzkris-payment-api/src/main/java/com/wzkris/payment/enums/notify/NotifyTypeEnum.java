package com.wzkris.payment.enums.notify;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 回调/通知类型
 *
 * @author wzkris
 */
@Getter
@AllArgsConstructor
public enum NotifyTypeEnum {
    PAY("PAY", "支付"),
    REFUND("REFUND", "退款");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

    @JsonCreator
    @Nullable
    public static NotifyTypeEnum fromValue(String value) {
        for (NotifyTypeEnum type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}
