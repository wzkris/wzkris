package com.wzkris.payment.controller.customerbalance;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.payment.api.customerbalance.CustomerBalanceInfoApi;
import com.wzkris.payment.api.customerbalance.request.CustomerBalanceTransactionLogPageRequest;
import com.wzkris.payment.api.customerbalance.response.CustomerBalanceInfoQueryResponse;
import com.wzkris.payment.api.customerbalance.response.CustomerBalanceTransactionLogInfoPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "客户余额信息")
@Validated
@RestController
@RequestMapping("/customer-balance-info")
@RequiredArgsConstructor
public class CustomerBalanceInfoController {

    private final CustomerBalanceInfoApi customerBalanceInfoApi;

    @Operation(summary = "余额信息")
    @GetMapping("/query")
    public Result<CustomerBalanceInfoQueryResponse> query() {
        return customerBalanceInfoApi.query();
    }

    @Operation(summary = "余额记录")
    @GetMapping("/query-transaction-page")
    public Result<Page<CustomerBalanceTransactionLogInfoPageResponse>> queryTransactionPage(@ParameterObject CustomerBalanceTransactionLogPageRequest request) {
        return customerBalanceInfoApi.queryTransactionPage(request);
    }

}