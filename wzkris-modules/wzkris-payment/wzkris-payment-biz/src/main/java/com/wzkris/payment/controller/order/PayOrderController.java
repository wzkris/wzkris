package com.wzkris.payment.controller.order;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.payment.api.order.PayOrderApi;
import com.wzkris.payment.api.order.request.PrepayRequest;
import com.wzkris.payment.api.order.response.PayOrderResponse;
import com.wzkris.payment.api.order.response.PrepayResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付订单（业务方调用）：统一下单 + 订单查询
 *
 * <p>TODO P1：业务方鉴权（CLIENT 凭证 + IP 白名单），当前 P0 仅登录可用
 *
 * @author wzkris
 */
@Tag(name = "支付订单")
@Validated
@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PayOrderController {

    private final PayOrderApi orderApi;

    @Operation(summary = "统一下单")
    @PostMapping("/prepay")
    public Result<PrepayResponse> prepay(@Validated @RequestBody PrepayRequest request) {
        return orderApi.prepay(request);
    }

    @Operation(summary = "按订单ID查询")
    @GetMapping("/query")
    public Result<PayOrderResponse> queryById(@ParameterObject IdRequest request) {
        return orderApi.queryByPayOrderId(request);
    }

    @Operation(summary = "按业务查询")
    @GetMapping("/query-by-biz")
    public Result<PayOrderResponse> queryByBiz(@RequestParam String bizType, @RequestParam String bizNo) {
        return orderApi.queryByBiz(bizType, bizNo);
    }
}
