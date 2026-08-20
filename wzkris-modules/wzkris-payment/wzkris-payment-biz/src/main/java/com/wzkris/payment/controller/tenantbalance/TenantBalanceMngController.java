package com.wzkris.payment.controller.tenantbalance;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.payment.api.tenantbalance.TenantBalanceMngApi;
import com.wzkris.payment.api.tenantbalance.request.TenantBalanceTransactionLogMngPageRequest;
import com.wzkris.payment.api.tenantbalance.response.TenantBalanceTransactionLogMngPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "租户余额管理")
@Validated
@RestController
@RequestMapping("/tenant-balance-manage")
@RequiredArgsConstructor
public class TenantBalanceMngController {

    private final TenantBalanceMngApi tenantBalanceMngApi;

    @Operation(summary = "余额记录分页")
    @GetMapping("/query-transaction-page")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "payment-mod:tenant-balance-mng:record-page")
    public Result<Page<TenantBalanceTransactionLogMngPageResponse>> queryTransactionPage(@ParameterObject TenantBalanceTransactionLogMngPageRequest request) {
        return tenantBalanceMngApi.queryTransactionPage(request);
    }

}