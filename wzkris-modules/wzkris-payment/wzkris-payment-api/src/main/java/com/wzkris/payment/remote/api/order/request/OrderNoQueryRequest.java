package com.wzkris.payment.remote.api.order.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 订单号参数体（单值载体）
 *
 * @author wzkris
 */
@Data
@Schema(description = "订单号参数体")
public class OrderNoQueryRequest {

    @NotBlank(message = "订单号不能为空")
    @Schema(description = "订单号")
    private String orderNo;

}
