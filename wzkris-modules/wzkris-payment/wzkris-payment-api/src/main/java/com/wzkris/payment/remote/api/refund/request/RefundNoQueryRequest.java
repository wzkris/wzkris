package com.wzkris.payment.remote.api.refund.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 退款号参数体（单值载体）
 *
 * @author wzkris
 */
@Data
@Schema(description = "退款号参数体")
public class RefundNoQueryRequest {

    @NotBlank(message = "退款号不能为空")
    @Schema(description = "退款号")
    private String refundNo;

}