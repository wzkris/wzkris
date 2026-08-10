package com.wzkris.payment.remote.api.refund.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

/**
 * 退款申请参数体
 *
 * @author wzkris
 */
@Data
@Schema(description = "退款申请参数体")
public class RefundApplyRequest {

    @NotBlank(message = "支付订单号不能为空")
    @Schema(description = "原支付订单号")
    private String orderNo;

    @NotNull(message = "退款金额不能为空")
    @DecimalMin(value = "0.01", message = "退款金额必须大于0")
    @Schema(description = "退款金额（元）")
    private BigDecimal refundAmount;

    @Schema(description = "退款原因")
    private String reason;

    @URL(message = "退款通知地址不能为空")
    @Schema(description = "退款业务方通知地址")
    private String notifyUrl;

}
