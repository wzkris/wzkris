package com.wzkris.payment.remote.api.refund;

import com.wzkris.common.core.model.Result;
import com.wzkris.payment.remote.api.refund.request.RefundApplyRequest;
import com.wzkris.payment.remote.api.refund.request.RefundNoQueryRequest;
import com.wzkris.payment.remote.api.refund.response.RefundQueryResponse;

/**
 * 退款内部接口（服务间调用）：申请退款 + 查询退款状态
 *
 * @author wzkris
 */
public interface RefundRemoteApi {

    /**
     * 申请退款
     */
    Result<RefundQueryResponse> apply(RefundApplyRequest request);

    /**
     * 按退款号查询退款状态
     */
    Result<RefundQueryResponse> queryByRefundNo(RefundNoQueryRequest request);

}
