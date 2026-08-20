package com.wzkris.usercenter.controller.tenant;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.usercenter.api.tenant.TenantInfoApi;
import com.wzkris.usercenter.api.tenant.request.TenantInfoBasicUpdateRequest;
import com.wzkris.usercenter.api.tenant.response.TenantInfoQueryResponse;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "租户信息")
@Validated
@RestController
@CheckPerms(checkTypes = AuthTypeEnum.TENANT, value = "user-mod:tenant-info")
@RequestMapping("/tenant-info")
@RequiredArgsConstructor
public class TenantInfoController {

    private final TenantInfoApi tenantInfoApi;

    @Operation(summary = "获取信息")
    @GetMapping("/query")
    public Result<TenantInfoQueryResponse> query() {
        return tenantInfoApi.query();
    }

    @Operation(summary = "修改信息")
    @PostMapping("/update-basic")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, value = "user-mod:tenant-info:edit")
    public Result<Void> updateBasicInfo(@RequestBody TenantInfoBasicUpdateRequest request) {
        return tenantInfoApi.updateBasicInfo(request);
    }

    @Operation(summary = "修改操作密码")
    @OperateLog(title = "商户信息", subTitle = "修改操作密码", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-operpwd")
    public Result<Void> updateOperPwd(@RequestBody @Validated(PasswordUpdateRequest.OperPwd.class) PasswordUpdateRequest request) {
        return tenantInfoApi.updateOperPwd(request);
    }

}

