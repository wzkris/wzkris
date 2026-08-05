package com.wzkris.payment.enums.channel;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 支付渠道
 *
 * @author wzkris
 */
@Getter
@AllArgsConstructor
public enum PayChannelEnum {
    WXPAY("WXPAY", "微信支付"),
    ALIPAY("ALIPAY", "支付宝");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

    @JsonCreator
    @Nullable
    public static PayChannelEnum fromValue(String value) {
        for (PayChannelEnum channel : values()) {
            if (channel.value.equals(value)) {
                return channel;
            }
        }
        return null;
    }
}
