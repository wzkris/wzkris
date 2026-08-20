package com.wzkris.payment.api.refund;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.payment.api.refund.request.RefundMngPageRequest;
import com.wzkris.payment.api.refund.response.RefundOrderResponse;

/**
 * 退款订单管理接口（后台）
 *
 * @author wzkris
 */
public interface RefundMngApi {

    Result<Page<RefundOrderResponse>> queryPage(RefundMngPageRequest request);

    Result<RefundOrderResponse> queryById(IdRequest request);

}
