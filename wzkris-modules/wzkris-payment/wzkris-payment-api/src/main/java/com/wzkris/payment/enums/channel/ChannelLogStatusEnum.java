package com.wzkris.payment.enums.channel;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 渠道交互留痕状态（区别于订单支付状态 PayStatusEnum，仅描述一次渠道调用的成败）
 *
 * @author wzkris
 */
@Getter
@AllArgsConstructor
public enum ChannelLogStatusEnum {
    SUCCESS("SUCCESS", "交互成功"),
    FAILED("FAILED", "交互失败");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

    @JsonCreator
    @Nullable
    public static ChannelLogStatusEnum fromValue(String value) {
        for (ChannelLogStatusEnum status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}
