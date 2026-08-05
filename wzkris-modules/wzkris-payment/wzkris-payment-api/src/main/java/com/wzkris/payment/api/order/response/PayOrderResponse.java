package com.wzkris.payment.api.order.response;

import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.pay.PayModeEnum;
import com.wzkris.payment.enums.pay.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * 支付订单响应
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class PayOrderResponse {

    private Long payOrderId;

    private String orderNo;

    private String bizType;

    private String bizNo;

    private PayChannelEnum channel;

    @Schema(description = "成交配置ID快照")
    private Long configId;

    private PayModeEnum payMode;

    private String subject;

    private BigDecimal amount;

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "已退款金额(含退款中)")
    private BigDecimal refundedAmount;

    private PayStatusEnum status;

    @Schema(description = "支付者标识")
    private String payerId;

    @Schema(description = "渠道侧交易号")
    private String channelOrderNo;

    @Schema(description = "支付成功时间")
    private OffsetDateTime payAt;

    @Schema(description = "过期时间")
    private OffsetDateTime expireAt;

    @Schema(description = "失败原因")
    private String failReason;
}
