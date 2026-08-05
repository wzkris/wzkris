package com.wzkris.payment.api.refund.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 退款申请参数体
 *
 * @author wzkris
 */
@Data
@Schema(description = "退款申请参数体")
public class RefundApplyRequest {

    @NotNull(message = "支付订单ID不能为空")
    @Schema(description = "原支付订单ID")
    private Long payOrderId;

    @NotNull(message = "退款金额不能为空")
    @DecimalMin(value = "0.01", message = "退款金额必须大于0")
    @Schema(description = "退款金额（元）")
    private BigDecimal refundAmount;

    @Schema(description = "退款原因")
    private String reason;
}
