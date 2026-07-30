package com.wzkris.usercenter.controller.tenantwallet;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.usercenter.api.tenantwallet.TenantWalletMngApi;
import com.wzkris.usercenter.api.tenantwallet.request.TenantWalletRecordMngPageRequest;
import com.wzkris.usercenter.api.tenantwallet.response.TenantWalletRecordResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "租户钱包管理")
@Validated
@RestController
@RequestMapping("/tenant-wallet-manage")
@RequiredArgsConstructor
public class TenantWalletMngController {

    private final TenantWalletMngApi tenantWalletMngApi;

    @Operation(summary = "钱包记录分页")
    @GetMapping("/record/page")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "user-mod:tenant-wallet-mng:record-page")
    public Result<Page<TenantWalletRecordResponse>> queryRecordPage(@ParameterObject TenantWalletRecordMngPageRequest request) {
        return tenantWalletMngApi.queryRecordPage(request);
    }

}
