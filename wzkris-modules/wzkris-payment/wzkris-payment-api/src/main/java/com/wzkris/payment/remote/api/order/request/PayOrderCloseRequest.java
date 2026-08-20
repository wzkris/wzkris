package com.wzkris.payment.remote.api.order.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 关单参数体
 *
 * <p>id 与 orderNo 二选一，由编排层校验至少传一个
 *
 * @author wzkris
 */
@Data
@Schema(description = "关单参数体")
public class PayOrderCloseRequest {

    @Schema(description = "支付订单ID（与orderNo二选一）")
    private Long id;

    @Schema(description = "订单号（与id二选一）")
    private String orderNo;

}
