package com.wzkris.payment.controller.tenantbalance;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.payment.api.tenantbalance.TenantBalanceInfoApi;
import com.wzkris.payment.api.tenantbalance.request.BalanceWithdrawalRequest;
import com.wzkris.payment.api.tenantbalance.request.SetPayPasswordRequest;
import com.wzkris.payment.api.tenantbalance.request.TenantBalanceTransactionLogInfoPageRequest;
import com.wzkris.payment.api.tenantbalance.response.TenantBalanceInfoQueryResponse;
import com.wzkris.payment.api.tenantbalance.response.TenantBalanceTransactionLogInfoPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "租户余额信息")
@Validated
@RestController
@RequestMapping("/tenant-balance")
@CheckPerms(checkTypes = AuthTypeEnum.TENANT, value = "payment-mod:tenant-balance-info")
@RequiredArgsConstructor
public class TenantBalanceInfoController {

    private final TenantBalanceInfoApi tenantBalanceInfoApi;

    @Operation(summary = "余额信息")
    @GetMapping("/query")
    public Result<TenantBalanceInfoQueryResponse> query() {
        return tenantBalanceInfoApi.query();
    }

    @Operation(summary = "余额记录分页")
    @GetMapping("/query-transaction-page")
    public Result<Page<TenantBalanceTransactionLogInfoPageResponse>> queryTransactionPage(@ParameterObject TenantBalanceTransactionLogInfoPageRequest request) {
        return tenantBalanceInfoApi.queryTransactionPage(request);
    }

    @Operation(summary = "设置提现支付密码")
    @OperateLog(title = "租户余额", subTitle = "设置支付密码", type = OperateTypeEnum.OTHER)
    @PostMapping("/set-pay-password")
    public Result<Void> setPayPassword(@RequestBody @Valid SetPayPasswordRequest request) {
        return tenantBalanceInfoApi.setPayPassword(request);
    }

    @Operation(summary = "提现")
    @OperateLog(title = "租户余额", subTitle = "提现", type = OperateTypeEnum.OTHER)
    @PostMapping("/withdrawal")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, value = "payment-mod:tenant-balance-info:withdrawal")
    public Result<Void> withdrawal(@RequestBody @Valid BalanceWithdrawalRequest request) {
        return tenantBalanceInfoApi.withdrawal(request);
    }

}