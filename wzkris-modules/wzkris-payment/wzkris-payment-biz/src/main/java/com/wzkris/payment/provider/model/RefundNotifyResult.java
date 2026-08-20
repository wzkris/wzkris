package com.wzkris.payment.provider.model;

import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * 退款回调解析结果（REFUND 专属字段）
 *
 * @author wzkris
 */
@Getter
@Setter
@NoArgsConstructor
public final class RefundNotifyResult extends AbsNotifyResult {

    /**
     * 我方退款号（即 refund_no）
     */
    private String outRefundNo;

    /**
     * 渠道侧是否退款成功
     */
    private boolean refundSuccess;

    private BigDecimal refundAmount;

    private OffsetDateTime refundAt;

    @Override
    public NotifyTypeEnum notifyType() {
        return NotifyTypeEnum.REFUND;
    }

    @Override
    public String outBusinessNo() {
        return outRefundNo;
    }

}
