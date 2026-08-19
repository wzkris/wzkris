package com.wzkris.payment.provider.model;

import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "我方订单号（即 order_no）")
    private String outTradeNo;

    @Schema(description = "渠道侧是否支付成功")
    private boolean paid;

    @Schema(description = "支付金额(元)")
    private BigDecimal amount;

    @Schema(description = "支付成功时间")
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
