package com.wzkris.usercenter.controller.tenant;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.security.annotation.CheckTenantPerms;
import com.wzkris.usercenter.api.tenant.TenantInfoApi;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.request.tenant.TenantInfoBasicUpdateRequest;
import com.wzkris.usercenter.response.tenant.TenantInfoResponse;
import com.wzkris.usercenter.response.tenant.TenantUsedQuotaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 自身租户信息
 *
 * @author wzkris
 */
@Tag(name = "租户信息")
@Validated
@RestController
@CheckTenantPerms("user-mod:tenant-info")
@RequestMapping("/tenant-info")
@RequiredArgsConstructor
public class TenantInfoController {

    private final TenantInfoApi tenantInfoApi;

    @Operation(summary = "获取信息")
    @GetMapping("/query-info")
    public Result<TenantInfoResponse> queryInfo() {
        return tenantInfoApi.queryInfo();
    }

    @Operation(summary = "修改信息")
    @PostMapping("/update-basic")
    @CheckTenantPerms("user-mod:tenant-info:edit")
    public Result<Void> updateBasicInfo(@RequestBody TenantInfoBasicUpdateRequest request) {
        return tenantInfoApi.updateBasicInfo(request);
    }

    @Operation(summary = "获取已使用配额")
    @GetMapping("/query-used-quota")
    public Result<TenantUsedQuotaResponse> queryLimitInfo() {
        return tenantInfoApi.queryLimitInfo();
    }

    @Operation(summary = "修改操作密码")
    @OperateLog(title = "商户信息", subTitle = "修改操作密码", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-operpwd")
    @PreAuthorize("@su.isSuper()")
    public Result<Void> updateOperPwd(@RequestBody @Validated(PasswordUpdateRequest.OperPwd.class) PasswordUpdateRequest request) {
        return tenantInfoApi.updateOperPwd(request);
    }

}

