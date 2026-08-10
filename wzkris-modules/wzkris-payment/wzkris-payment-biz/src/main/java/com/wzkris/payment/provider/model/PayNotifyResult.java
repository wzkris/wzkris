package com.wzkris.payment.provider.model;

import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * 支付回调解析结果（PAY 专属字段）
 *
 * @author wzkris
 */
@Getter
@Setter
@NoArgsConstructor
public final class PayNotifyResult extends AbsNotifyResult {

    /**
     * 我方订单号（即 order_no）
     */
    private String outTradeNo;

    /**
     * 渠道侧是否支付成功
     */
    private boolean paid;

    private BigDecimal amount;

    private OffsetDateTime payAt;

    @Override
    public NotifyTypeEnum notifyType() {
        return NotifyTypeEnum.PAY;
    }

    @Override
    public String outBusinessNo() {
        return outTradeNo;
    }

}
