package com.wzkris.payment.controller.refund;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.payment.api.refund.PayRefundMngApi;
import com.wzkris.payment.api.refund.request.PayRefundMngPageRequest;
import com.wzkris.payment.api.refund.response.RefundOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 退款订单管理（后台）
 *
 * @author wzkris
 */
@Tag(name = "退款订单管理")
@Validated
@RestController
@RequestMapping("/pay-refund-manage")
@RequiredArgsConstructor
public class PayRefundMngController {

    private static final String PERM_PREFIX = "pay-mod:refund-mng:";

    private final PayRefundMngApi refundMngApi;

    @Operation(summary = "退款订单分页")
    @GetMapping("/query-page")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "page")
    public Result<Page<RefundOrderResponse>> queryPage(@ParameterObject PayRefundMngPageRequest request) {
        return refundMngApi.queryPage(request);
    }

    @Operation(summary = "退款订单详情")
    @GetMapping("/query-info")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "page")
    public Result<RefundOrderResponse> queryInfo(@ParameterObject IdRequest request) {
        return refundMngApi.queryInfo(request);
    }
}
