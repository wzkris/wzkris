package com.wzkris.usercenter.controller.tenantwallet;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.usercenter.api.tenantwallet.TenantWalletInfoApi;
import com.wzkris.usercenter.api.tenantwallet.request.TenantWalletRecordInfoPageRequest;
import com.wzkris.usercenter.api.tenantwallet.request.WalletWithdrawalRequest;
import com.wzkris.usercenter.api.tenantwallet.response.TenantWalletInfoQueryResponse;
import com.wzkris.usercenter.api.tenantwallet.response.TenantWalletRecordInfoPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "租户钱包信息")
@Validated
@RestController
@RequestMapping("/tenant-wallet")
@CheckPerms(checkTypes = AuthTypeEnum.TENANT, value = "user-mod:tenant-wallet-info")
@RequiredArgsConstructor
public class TenantWalletInfoController {

    private final TenantWalletInfoApi tenantWalletInfoApi;

    @Operation(summary = "余额信息")
    @GetMapping("/query-info")
    public Result<TenantWalletInfoQueryResponse> queryInfo() {
        return tenantWalletInfoApi.queryInfo();
    }

    @Operation(summary = "钱包记录分页")
    @GetMapping("/query-record-page")
    public Result<Page<TenantWalletRecordInfoPageResponse>> queryRecordPage(@ParameterObject TenantWalletRecordInfoPageRequest request) {
        return tenantWalletInfoApi.queryRecordPage(request);
    }

    @Operation(summary = "提现")
    @OperateLog(title = "商户信息", subTitle = "提现", type = OperateTypeEnum.OTHER)
    @PostMapping("/withdrawal")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, value = "user-mod:tenant-wallet-info:withdrawal")
    public Result<Void> withdrawal(@RequestBody @Valid WalletWithdrawalRequest request) {
        return tenantWalletInfoApi.withdrawal(request);
    }

}

