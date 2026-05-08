package com.wzkris.usercenter.controller.tenant;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.usercenter.api.tenant.TenantMngApi;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.request.common.PwdResetRequest;
import com.wzkris.usercenter.request.tenant.TenantMngPageRequest;
import com.wzkris.usercenter.request.tenant.TenantMngSaveRequest;
import com.wzkris.usercenter.request.tenant.TenantMngUpdateRequest;
import com.wzkris.usercenter.request.tenantpackage.TenantPackageMngListRequest;
import com.wzkris.usercenter.response.common.SelectResponse;
import com.wzkris.usercenter.response.tenant.TenantMngResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "租户管理")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/tenant-manage")
public class TenantMngController {

    private final TenantMngApi tenantMngApi;

    @Operation(summary = "租户分页")
    @GetMapping("/query-page")
    @CheckAdminPerms("user-mod:tenant-mng:page")
    public Result<Page<TenantMngResponse>> queryPage(@ParameterObject TenantMngPageRequest request) {
        return tenantMngApi.queryPage(request);
    }

    @Operation(summary = "ID获取租户详细信息")
    @GetMapping("/query-info/{id}")
    @CheckAdminPerms("user-mod:tenant-mng:page")
    public Result<TenantMngResponse> queryInfo(@ParameterObject IdRequest request) {
        return tenantMngApi.queryInfo(request);
    }

    @Operation(summary = "租户选择列表(带分页)")
    @GetMapping("/query-selectpage")
    public Result<Page<SelectResponse>> querySelectPage(@ParameterObject TenantMngPageRequest request) {
        return tenantMngApi.querySelectPage(request);
    }

    @Operation(summary = "套餐选择列表")
    @GetMapping("/query-package-select")
    @CheckAdminPerms(
            value = {"user-mod:tenant-mng:add", "user-mod:tenant-mng:edit"},
            mode = CheckMode.OR)
    public Result<List<SelectResponse>> queryPackageSelect(@ParameterObject TenantPackageMngListRequest request) {
        return tenantMngApi.queryPackageSelect(request);
    }

    @Operation(summary = "新增租户")
    @OperateLog(title = "租户管理", subTitle = "新增租户", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckAdminPerms("user-mod:tenant-mng:add")
    public Result<Void> save(@Validated @RequestBody TenantMngSaveRequest tenantReq) {
        return tenantMngApi.save(tenantReq);
    }

    @Operation(summary = "修改租户")
    @OperateLog(title = "租户管理", subTitle = "修改租户", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckAdminPerms("user-mod:tenant-mng:edit")
    public Result<Void> update(@Validated @RequestBody TenantMngUpdateRequest tenantReq) {
        return tenantMngApi.update(tenantReq);
    }

    @Operation(summary = "重置租户操作密码")
    @OperateLog(title = "租户管理", subTitle = "重置操作密码", type = OperateTypeEnum.UPDATE)
    @PostMapping("/reset-operpwd")
    @CheckAdminPerms("user-mod:tenant-mng:reset-operpwd")
    public Result<Void> resetOperPwd(@RequestBody PwdResetRequest request) {
        return tenantMngApi.resetOperPwd(request);
    }

    @Operation(summary = "删除租户")
    @OperateLog(title = "租户管理", subTitle = "删除租户", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("user-mod:tenant-mng:remove")
    public Result<Void> remove(@RequestBody @Valid IdRequest request) {
        return tenantMngApi.remove(request);
    }

}

