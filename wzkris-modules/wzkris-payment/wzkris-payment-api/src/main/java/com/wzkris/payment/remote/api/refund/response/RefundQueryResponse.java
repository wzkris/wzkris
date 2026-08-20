package com.wzkris.payment.remote.api.refund.response;

import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.refund.RefundStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * 退款订单查询响应
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class RefundQueryResponse {

    private Long id;

    private String refundNo;

    private Long payOrderId;

    private PayChannelEnum channel;

    @Schema(description = "退款所用配置ID快照")
    private Long configId;

    private BigDecimal refundAmount;

    private RefundStatusEnum status;

    @Schema(description = "渠道侧退款单号")
    private String channelRefundNo;

    @Schema(description = "退款成功时间")
    private OffsetDateTime refundAt;

    @Schema(description = "失败原因")
    private String failReason;

}
