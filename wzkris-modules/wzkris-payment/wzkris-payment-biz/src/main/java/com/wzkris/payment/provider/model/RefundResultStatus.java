package com.wzkris.payment.provider.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 渠道退款结果状态
 *
 * <p>微信退款为受理制：refund() 返回受理结果，真正到账需等异步退款回调，
 * 故区分 SUCCESS / PROCESSING / FAILED 三态。
 *
 * @author wzkris
 */
@Getter
@AllArgsConstructor
public enum RefundResultStatus {

    SUCCESS("SUCCESS", "退款成功"),
    PROCESSING("PROCESSING", "退款受理中,等待异步回调终结"),
    FAILED("FAILED", "退款失败");

    private final String value;

    private final String description;
}
