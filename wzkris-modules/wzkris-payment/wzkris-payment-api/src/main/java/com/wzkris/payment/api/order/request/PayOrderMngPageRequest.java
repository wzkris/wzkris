package com.wzkris.payment.api.order.request;

import com.wzkris.common.orm.request.PagingRequest;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.pay.PayStatusEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付订单分页查询参数体
 *
 * @author wzkris
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "支付订单分页查询参数体")
public class PayOrderMngPageRequest extends PagingRequest {

    @Parameter(description = "订单号")
    private String orderNo;

    @Parameter(description = "支付渠道")
    private PayChannelEnum channel;

    @Parameter(description = "订单状态")
    private PayStatusEnum status;

}
