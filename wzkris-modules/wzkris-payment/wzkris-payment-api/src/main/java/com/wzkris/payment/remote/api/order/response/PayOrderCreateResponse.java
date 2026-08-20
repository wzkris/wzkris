package com.wzkris.payment.remote.api.order.response;

import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.pay.PayModeEnum;
import com.wzkris.payment.enums.pay.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * 生成订单并支付响应
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class PayOrderCreateResponse {

    private Long id;

    @Schema(description = "订单号（商户自行存储，凭此查询/退款）")
    private String orderNo;

    private PayChannelEnum channel;

    private PayModeEnum payMode;

    private PayStatusEnum status;

    @Schema(description = "过期时间")
    private OffsetDateTime expireAt;

    @Schema(description = "渠道侧支付参数（前端据此唤起支付）")
    private String prepayPayload;

}
