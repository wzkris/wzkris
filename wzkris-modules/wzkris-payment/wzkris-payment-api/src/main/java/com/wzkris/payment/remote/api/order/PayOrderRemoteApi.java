package com.wzkris.payment.remote.api.order;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.payment.remote.api.order.request.OrderNoQueryRequest;
import com.wzkris.payment.remote.api.order.request.PayOrderCloseRequest;
import com.wzkris.payment.remote.api.order.request.PayOrderCreateRequest;
import com.wzkris.payment.remote.api.order.response.PayOrderCreateResponse;
import com.wzkris.payment.remote.api.order.response.PayOrderQueryResponse;

/**
 * 支付订单内部接口（服务间调用）：生成订单并发起支付 + 订单查询 + 主动关单
 *
 * @author wzkris
 */
public interface PayOrderRemoteApi {

    /**
     * 生成订单并发起支付：登记订单意图 -> 渠道预下单，返回渠道侧支付参数
     */
    Result<PayOrderCreateResponse> create(PayOrderCreateRequest request);

    /**
     * 按支付订单ID查询
     */
    Result<PayOrderQueryResponse> queryById(IdRequest request);

    /**
     * 按订单号查询
     */
    Result<PayOrderQueryResponse> queryByOrderNo(OrderNoQueryRequest request);

    /**
     * 主动关单（id/orderNo 二选一，仅 PENDING 可关）
     */
    Result<Void> close(PayOrderCloseRequest request);

}
