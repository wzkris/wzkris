package com.wzkris.payment.api.order.response;

import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.pay.PayModeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一下单响应
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class PrepayResponse {

    private Long id;

    private String orderNo;

    private PayChannelEnum channel;

    private PayModeEnum payMode;

    @Schema(description = "渠道侧支付参数（前端据此唤起支付）")
    private String prepayPayload;
}
