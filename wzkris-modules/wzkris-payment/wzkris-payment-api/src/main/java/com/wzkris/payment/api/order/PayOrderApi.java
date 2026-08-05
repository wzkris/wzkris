package com.wzkris.payment.api.order;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.payment.api.order.request.PrepayRequest;
import com.wzkris.payment.api.order.response.PayOrderResponse;
import com.wzkris.payment.api.order.response.PrepayResponse;

/**
 * 支付订单接口（业务方调用）：统一下单 + 订单查询
 *
 * @author wzkris
 */
public interface PayOrderApi {

    /**
     * 统一下单，返回渠道侧支付参数
     */
    Result<PrepayResponse> prepay(PrepayRequest request);

    /**
     * 按支付订单ID查询
     */
    Result<PayOrderResponse> queryByPayOrderId(IdRequest request);

    /**
     * 按业务类型 + 业务订单号查询
     */
    Result<PayOrderResponse> queryByBiz(String bizType, String bizNo);
}
