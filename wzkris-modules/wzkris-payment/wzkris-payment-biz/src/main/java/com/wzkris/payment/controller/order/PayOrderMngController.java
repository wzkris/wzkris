package com.wzkris.payment.controller.order;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.payment.api.order.PayOrderMngApi;
import com.wzkris.payment.api.order.request.PayOrderMngPageRequest;
import com.wzkris.payment.api.order.response.PayOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付订单管理（后台）
 *
 * @author wzkris
 */
@Tag(name = "支付订单管理")
@Validated
@RestController
@RequestMapping("/pay-order-manage")
@RequiredArgsConstructor
public class PayOrderMngController {

    private static final String PERM_PREFIX = "pay-mod:order-mng:";

    private final PayOrderMngApi orderMngApi;

    @Operation(summary = "支付订单分页")
    @GetMapping("/query-page")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "page")
    public Result<Page<PayOrderResponse>> queryPage(@ParameterObject PayOrderMngPageRequest request) {
        return orderMngApi.queryPage(request);
    }

    @Operation(summary = "支付订单详情")
    @GetMapping("/query-info")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "page")
    public Result<PayOrderResponse> queryInfo(@ParameterObject IdRequest request) {
        return orderMngApi.queryInfo(request);
    }

    @Operation(summary = "主动关单")
    @OperateLog(title = "支付订单管理", subTitle = "关单", type = OperateTypeEnum.UPDATE)
    @PostMapping("/close")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "close")
    public Result<Void> close(@ParameterObject IdRequest request) {
        return orderMngApi.close(request);
    }
}
