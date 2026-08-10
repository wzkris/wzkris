package com.wzkris.payment.remote.controller.order;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.payment.remote.api.order.PayOrderRemoteApi;
import com.wzkris.payment.remote.api.order.request.OrderNoQueryRequest;
import com.wzkris.payment.remote.api.order.request.PayOrderCloseRequest;
import com.wzkris.payment.remote.api.order.request.PayOrderCreateRequest;
import com.wzkris.payment.remote.api.order.response.PayOrderCreateResponse;
import com.wzkris.payment.remote.api.order.response.PayOrderQueryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付订单内部接口（服务间调用）：生成订单并发起支付 + 订单查询 + 主动关单
 *
 * @author wzkris
 */
@Tag(name = "支付订单（内部）")
@Validated
@RestController
@RequestMapping("/pay-order-remote")
@RequiredArgsConstructor
public class PayOrderRemoteController {

    private final PayOrderRemoteApi payOrderRemoteApi;

    @Operation(summary = "生成订单并发起支付")
    @PostMapping("/create")
    public Result<PayOrderCreateResponse> create(@Validated @RequestBody PayOrderCreateRequest request) {
        return payOrderRemoteApi.create(request);
    }

    @Operation(summary = "按订单ID查询")
    @PostMapping("/query-by-id")
    public Result<PayOrderQueryResponse> queryById(@Validated @RequestBody IdRequest request) {
        return payOrderRemoteApi.queryById(request);
    }

    @Operation(summary = "按订单号查询")
    @PostMapping("/query-by-no")
    public Result<PayOrderQueryResponse> queryByOrderNo(@Validated @RequestBody OrderNoQueryRequest request) {
        return payOrderRemoteApi.queryByOrderNo(request);
    }

    @Operation(summary = "主动关单")
    @PostMapping("/close")
    public Result<Void> close(@Validated @RequestBody PayOrderCloseRequest request) {
        return payOrderRemoteApi.close(request);
    }

}
