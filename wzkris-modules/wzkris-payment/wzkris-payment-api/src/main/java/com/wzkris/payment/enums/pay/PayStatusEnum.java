package com.wzkris.payment.enums.pay;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 支付订单状态
 *
 * @author wzkris
 */
@Getter
@AllArgsConstructor
public enum PayStatusEnum {
    PENDING("PENDING", "待支付"),
    SUCCESS("SUCCESS", "已支付"),
    CLOSED("CLOSED", "已关闭"),
    FAILED("FAILED", "支付失败");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

    @JsonCreator
    @Nullable
    public static PayStatusEnum fromValue(String value) {
        for (PayStatusEnum status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}
