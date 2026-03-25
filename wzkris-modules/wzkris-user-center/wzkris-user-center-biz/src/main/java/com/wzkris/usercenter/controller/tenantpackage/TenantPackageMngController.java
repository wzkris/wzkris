package com.wzkris.usercenter.controller.tenantpackage;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.usercenter.api.tenantpackage.TenantPackageMngApi;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.tenantpackage.TenantPackageMngQueryRequest;
import com.wzkris.usercenter.request.tenantpackage.TenantPackageMngSaveRequest;
import com.wzkris.usercenter.request.tenantpackage.TenantPackageMngUpdateRequest;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;
import com.wzkris.usercenter.response.tenantpackage.TenantPackageInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户套餐管理
 *
 * @author wzkris
 */
@Tag(name = "租户套餐管理")
@Validated
@RestController
@RequestMapping("/tenant-package-manage")
@RequiredArgsConstructor
public class TenantPackageMngController extends BaseController {

    private final TenantPackageMngApi tenantPackageMngApi;

    @Operation(summary = "套餐分页")
    @GetMapping("/query-page")
    @CheckAdminPerms("user-mod:tenantpackage-mng:page")
    public Result<Page<TenantPackageInfoResponse>> queryPage(TenantPackageMngQueryRequest request) {
        return tenantPackageMngApi.queryPage(request);
    }

    @Operation(summary = "套餐详细信息")
    @GetMapping("/query-info/{packageId}")
    @CheckAdminPerms("user-mod:tenantpackage-mng:page")
    public Result<TenantPackageInfoResponse> queryInfo(@PathVariable Long packageId) {
        return tenantPackageMngApi.queryInfo(packageId);
    }

    @Operation(summary = "套餐菜单选择树")
    @GetMapping({"/query-menu-checked-selecttree/", "/query-menu-checked-selecttree/{packageId}"})
    @CheckAdminPerms(
            value = {"user-mod:tenantpackage-mng:add", "user-mod:tenantpackage-mng:edit"},
            mode = CheckMode.OR)
    public Result<CheckedSelectTreeResponse> queryMenuSelectTree(@PathVariable(required = false) Long packageId) {
        return tenantPackageMngApi.queryMenuSelectTree(packageId);
    }

    @Operation(summary = "新增租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "新增套餐", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckAdminPerms("user-mod:tenantpackage-mng:add")
    public Result<Void> save(@Valid @RequestBody TenantPackageMngSaveRequest request) {
        return tenantPackageMngApi.save(request);
    }

    @Operation(summary = "修改租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "修改套餐", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckAdminPerms("user-mod:tenantpackage-mng:edit")
    public Result<Void> update(@Valid @RequestBody TenantPackageMngUpdateRequest request) {
        return tenantPackageMngApi.update(request);
    }

    @Operation(summary = "修改租户套餐状态")
    @OperateLog(title = "租户套餐", subTitle = "修改租户套餐状态", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-status")
    @CheckAdminPerms("user-mod:tenantpackage-mng:edit")
    public Result<Void> updateStatus(@RequestBody @Valid StatusUpdateRequest request) {
        return tenantPackageMngApi.updateStatus(request);
    }

    @Operation(summary = "删除租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "删除套餐", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("user-mod:tenantpackage-mng:remove")
    public Result<Void> remove(
            @NotEmpty(message = "{invalidParameter.id.invalid}") @RequestBody List<Long> packageIds) {
        return tenantPackageMngApi.remove(packageIds);
    }

}

