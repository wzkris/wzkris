package com.wzkris.payment.api.order;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.payment.api.order.request.PayOrderMngPageRequest;
import com.wzkris.payment.api.order.response.PayOrderResponse;

/**
 * 支付订单管理接口（后台）
 *
 * @author wzkris
 */
public interface PayOrderMngApi {

    Result<Page<PayOrderResponse>> queryPage(PayOrderMngPageRequest request);

    Result<PayOrderResponse> queryById(IdRequest request);

}
