package com.wzkris.payment.controller.refund;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.payment.api.refund.PayRefundApi;
import com.wzkris.payment.api.refund.request.RefundApplyRequest;
import com.wzkris.payment.api.refund.response.RefundOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 退款（业务方调用）
 *
 * @author wzkris
 */
@Tag(name = "退款")
@Validated
@RestController
@RequestMapping("/refund")
@RequiredArgsConstructor
public class PayRefundController {

    private final PayRefundApi refundApi;

    @Operation(summary = "申请退款")
    @PostMapping("/apply")
    public Result<RefundOrderResponse> apply(@Validated @RequestBody RefundApplyRequest request) {
        return refundApi.apply(request);
    }

    @Operation(summary = "查询退款")
    @GetMapping("/query")
    public Result<RefundOrderResponse> query(@ParameterObject IdRequest request) {
        return refundApi.queryRefund(request);
    }
}
