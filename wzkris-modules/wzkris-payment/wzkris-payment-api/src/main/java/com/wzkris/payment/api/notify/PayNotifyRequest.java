package com.wzkris.payment.api.notify;

import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import com.wzkris.payment.enums.pay.PayStatusEnum;
import com.wzkris.payment.enums.refund.RefundStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * 业务方结果通知报文（网关 -> 业务方）
 *
 * <p>notifyType=PAY 携带支付结果；notifyType=REFUND 携带退款结果。
 * <p>业务方按此契约实现通知接收端点，地址由下单时约定或按 bizType 配置。
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class PayNotifyRequest {

    @Schema(description = "通知类型 PAY/REFUND")
    private NotifyTypeEnum notifyType;

    private Long payOrderId;

    private String orderNo;

    private String bizType;

    private String bizNo;

    private PayChannelEnum channel;

    // ---- PAY 上下文 ----

    @Schema(description = "支付金额(元)")
    private BigDecimal amount;

    @Schema(description = "支付订单状态")
    private PayStatusEnum status;

    @Schema(description = "渠道侧交易号")
    private String channelOrderNo;

    @Schema(description = "支付成功时间")
    private OffsetDateTime payAt;

    // ---- REFUND 上下文 ----

    @Schema(description = "退款订单ID")
    private Long refundOrderId;

    @Schema(description = "退款单号")
    private String refundNo;

    @Schema(description = "退款金额(元)")
    private BigDecimal refundAmount;

    @Schema(description = "退款状态")
    private RefundStatusEnum refundStatus;

    @Schema(description = "渠道侧退款单号")
    private String channelRefundNo;

    @Schema(description = "退款成功时间")
    private OffsetDateTime refundAt;
}
