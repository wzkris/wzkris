package com.wzkris.payment.remote.controller.balance;

import com.wzkris.common.core.model.Result;
import com.wzkris.payment.remote.api.balance.BalanceRemoteApi;
import com.wzkris.payment.remote.api.balance.request.BalanceDecryRequest;
import com.wzkris.payment.remote.api.balance.request.BalanceIncryRequest;
import com.wzkris.payment.remote.api.balance.request.BalanceQueryRequest;
import com.wzkris.payment.remote.api.balance.response.BalanceQueryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 余额/账户内部接口（服务间调用）：余额查询 + 加/减余额
 *
 * @author wzkris
 */
@Tag(name = "余额账户")
@Validated
@RestController
@RequestMapping("/balance-remote")
@RequiredArgsConstructor
public class BalanceRemoteController {

    private final BalanceRemoteApi balanceRemoteApi;

    @Operation(summary = "查询余额")
    @PostMapping("/query")
    public Result<BalanceQueryResponse> query(@Validated @RequestBody BalanceQueryRequest request) {
        return balanceRemoteApi.query(request);
    }

    @Operation(summary = "增加余额")
    @PostMapping("/incry")
    public Result<Void> incry(@Validated @RequestBody BalanceIncryRequest request) {
        return balanceRemoteApi.incry(request);
    }

    @Operation(summary = "扣减余额")
    @PostMapping("/decry")
    public Result<Void> decry(@Validated @RequestBody BalanceDecryRequest request) {
        return balanceRemoteApi.decry(request);
    }

}