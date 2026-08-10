package com.wzkris.payment.remote.controller.refund;

import com.wzkris.common.core.model.Result;
import com.wzkris.payment.remote.api.refund.RefundRemoteApi;
import com.wzkris.payment.remote.api.refund.request.RefundApplyRequest;
import com.wzkris.payment.remote.api.refund.request.RefundNoQueryRequest;
import com.wzkris.payment.remote.api.refund.response.RefundQueryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 退款内部接口（服务间调用）：申请退款 + 查询退款状态
 *
 * @author wzkris
 */
@Tag(name = "退款（内部）")
@Validated
@RestController
@RequestMapping("/refund-remote")
@RequiredArgsConstructor
public class RefundRemoteController {

    private final RefundRemoteApi refundRemoteApi;

    @Operation(summary = "申请退款")
    @PostMapping("/apply")
    public Result<RefundQueryResponse> apply(@Validated @RequestBody RefundApplyRequest request) {
        return refundRemoteApi.apply(request);
    }

    @Operation(summary = "按退款号查询退款")
    @PostMapping("/query-by-no")
    public Result<RefundQueryResponse> queryByRefundNo(@Validated @RequestBody RefundNoQueryRequest request) {
        return refundRemoteApi.queryByRefundNo(request);
    }

}
