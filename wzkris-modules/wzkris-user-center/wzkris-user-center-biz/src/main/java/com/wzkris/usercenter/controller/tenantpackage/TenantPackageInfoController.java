package com.wzkris.usercenter.controller.tenantpackage;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.usercenter.api.tenantpackage.TenantPackageInfoApi;
import com.wzkris.usercenter.api.tenantpackage.response.TenantPackageInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "租户套餐信息")
@Validated
@RestController
@CheckPerms(value = "user-mod:tenant-package-info", checkTypes = AuthTypeEnum.TENANT)
@RequestMapping("/tenant-package-info")
@RequiredArgsConstructor
public class TenantPackageInfoController {

    private final TenantPackageInfoApi tenantPackageInfoApi;

    @Operation(summary = "获取当前租户套餐概览")
    @GetMapping("/query-info")
    public Result<TenantPackageInfoResponse> queryInfo() {
        return tenantPackageInfoApi.queryInfo();
    }

}

