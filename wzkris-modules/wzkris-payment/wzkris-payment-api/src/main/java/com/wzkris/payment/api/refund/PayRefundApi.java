package com.wzkris.payment.api.refund;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.payment.api.refund.request.RefundApplyRequest;
import com.wzkris.payment.api.refund.response.RefundOrderResponse;

/**
 * 退款接口
 *
 * @author wzkris
 */
public interface PayRefundApi {

    /**
     * 申请退款
     */
    Result<RefundOrderResponse> apply(RefundApplyRequest request);

    /**
     * 查询退款状态
     */
    Result<RefundOrderResponse> queryRefund(IdRequest request);
}
