package com.wzkris.payment.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.refund.RefundStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * 退款订单
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "pay_refund_order")
public class PayRefundOrderDO extends BaseEntity {

    @Schema(description = "退款单号")
    private String refundNo;

    @Schema(description = "原支付订单ID")
    private Long payOrderId;

    @Schema(description = "支付渠道")
    private PayChannelEnum channel;

    @Schema(description = "退款所用配置ID快照")
    private Long configId;

    @Schema(description = "退款金额(元)")
    private BigDecimal refundAmount;

    @Schema(description = "退款状态")
    private RefundStatusEnum status;

    @Schema(description = "退款原因")
    private String reason;

    @Schema(description = "渠道侧退款单号")
    private String channelRefundNo;

    @Schema(description = "退款成功时间")
    private OffsetDateTime refundAt;

    @Schema(description = "失败原因")
    private String failReason;
}
