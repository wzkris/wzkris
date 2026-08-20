package com.wzkris.payment.enums.pay;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 支付方式
 *
 * @author wzkris
 */
@Getter
@AllArgsConstructor
public enum PayModeEnum {
    JSAPI("JSAPI", "JSAPI支付"),
    NATIVE("NATIVE", "扫码支付"),
    APP("APP", "APP支付"),
    H5("H5", "H5支付");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

    @JsonCreator
    @Nullable
    public static PayModeEnum fromValue(String value) {
        for (PayModeEnum mode : values()) {
            if (mode.value.equals(value)) {
                return mode;
            }
        }
        return null;
    }
}
