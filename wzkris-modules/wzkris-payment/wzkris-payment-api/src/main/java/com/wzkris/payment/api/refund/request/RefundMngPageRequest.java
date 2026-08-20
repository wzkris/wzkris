package com.wzkris.payment.api.refund.request;

import com.wzkris.common.orm.request.PagingRequest;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.refund.RefundStatusEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 退款订单分页查询参数体
 *
 * @author wzkris
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "退款订单分页查询参数体")
public class RefundMngPageRequest extends PagingRequest {

    @Parameter(description = "退款单号")
    private String refundNo;

    @Parameter(description = "原支付订单ID")
    private Long payOrderId;

    @Parameter(description = "支付渠道")
    private PayChannelEnum channel;

    @Parameter(description = "退款状态")
    private RefundStatusEnum status;

}
