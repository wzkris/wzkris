package com.wzkris.usercenter.controller.tenantwallet;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckTenantPerms;
import com.wzkris.usercenter.api.tenantwallet.TenantWalletInfoApi;
import com.wzkris.usercenter.request.tenantwallet.TenantWalletRecordQueryRequest;
import com.wzkris.usercenter.request.tenantwallet.WalletWithdrawalRequest;
import com.wzkris.usercenter.response.tenantwallet.TenantWalletInfoResponse;
import com.wzkris.usercenter.response.tenantwallet.TenantWalletRecordResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 租户钱包信息
 *
 * @author wzkris
 */
@Tag(name = "租户钱包信息")
@Validated
@RestController
@RequestMapping("/tenant-wallet")
@CheckTenantPerms("user-mod:tenant-wallet-info")
@RequiredArgsConstructor
public class TenantWalletInfoController extends BaseController {

    private final TenantWalletInfoApi tenantWalletInfoApi;

    @Operation(summary = "余额信息")
    @GetMapping("/query-info")
    public Result<TenantWalletInfoResponse> queryInfo() {
        return ok(tenantWalletInfoApi.queryInfo());
    }

    @Operation(summary = "钱包记录分页")
    @GetMapping("/query-record-page")
    public Result<Page<TenantWalletRecordResponse>> queryRecordPage(TenantWalletRecordQueryRequest request) {
        return tenantWalletInfoApi.queryRecordPage(request);
    }

    @Operation(summary = "提现")
    @OperateLog(title = "商户信息", subTitle = "提现", type = OperateTypeEnum.OTHER)
    @PostMapping("/withdrawal")
    @CheckTenantPerms("user-mod:tenant-wallet-info:withdrawal")
    public Result<Void> withdrawal(@RequestBody @Valid WalletWithdrawalRequest request) {
        return tenantWalletInfoApi.withdrawal(request);
    }

}

