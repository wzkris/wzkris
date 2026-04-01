package com.wzkris.usercenter.controller.tenantwallet;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.usercenter.api.tenantwallet.TenantWalletMngApi;
import com.wzkris.usercenter.request.tenantwallet.TenantWalletRecordMngQueryRequest;
import com.wzkris.usercenter.response.tenantwallet.TenantWalletRecordResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 租户钱包管理
 *
 * @author wzkris
 */
@Tag(name = "租户钱包管理")
@Validated
@RestController
@RequestMapping("/tenant-wallet-manage")
@RequiredArgsConstructor
public class TenantWalletMngController {

    private final TenantWalletMngApi tenantWalletMngApi;

    @Operation(summary = "钱包记录分页")
    @GetMapping("/record/page")
    @CheckAdminPerms("user-mod:tenant-wallet-mng:record-page")
    public Result<Page<TenantWalletRecordResponse>> queryRecordPage(TenantWalletRecordMngQueryRequest request) {
        return tenantWalletMngApi.queryRecordPage(request);
    }

}
