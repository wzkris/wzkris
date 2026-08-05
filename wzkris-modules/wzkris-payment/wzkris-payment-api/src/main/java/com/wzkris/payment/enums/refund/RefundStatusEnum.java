package com.wzkris.payment.enums.refund;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 退款状态
 *
 * @author wzkris
 */
@Getter
@AllArgsConstructor
public enum RefundStatusEnum {
    REFUNDING("REFUNDING", "退款中"),
    SUCCESS("SUCCESS", "退款成功"),
    FAILED("FAILED", "退款失败");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

    @JsonCreator
    @Nullable
    public static RefundStatusEnum fromValue(String value) {
        for (RefundStatusEnum status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}
