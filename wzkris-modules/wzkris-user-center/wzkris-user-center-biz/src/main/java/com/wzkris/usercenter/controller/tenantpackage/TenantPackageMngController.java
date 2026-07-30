package com.wzkris.usercenter.controller.tenantpackage;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.usercenter.api.tenantpackage.TenantPackageMngApi;
import com.wzkris.usercenter.api.tenantpackage.request.TenantPackageMngPageRequest;
import com.wzkris.usercenter.api.tenantpackage.request.TenantPackageMngSaveRequest;
import com.wzkris.usercenter.api.tenantpackage.request.TenantPackageMngUpdateRequest;
import com.wzkris.usercenter.api.tenantpackage.response.TenantPackageMngResponse;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "租户套餐管理")
@Validated
@RestController
@RequestMapping("/tenant-package-manage")
@RequiredArgsConstructor
public class TenantPackageMngController {

    private final TenantPackageMngApi tenantPackageMngApi;

    @Operation(summary = "套餐分页")
    @GetMapping("/query-page")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "user-mod:tenantpackage-mng:page")
    public Result<Page<TenantPackageMngResponse>> queryPage(@ParameterObject TenantPackageMngPageRequest request) {
        return tenantPackageMngApi.queryPage(request);
    }

    @Operation(summary = "套餐详细信息")
    @GetMapping("/query-info/{id}")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "user-mod:tenantpackage-mng:page")
    public Result<TenantPackageMngResponse> queryInfo(@ParameterObject IdRequest request) {
        return tenantPackageMngApi.queryInfo(request);
    }

    @Operation(summary = "套餐菜单选择树")
    @GetMapping({"/query-menu-checked-selecttree/", "/query-menu-checked-selecttree/{id}"})
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN,
            value = {"user-mod:tenantpackage-mng:add", "user-mod:tenantpackage-mng:edit"},
            mode = CheckMode.OR)
    public Result<CheckedSelectTreeResponse> queryMenuSelectTree(@ParameterObject IdRequest request) {
        return tenantPackageMngApi.queryMenuSelectTree(request);
    }

    @Operation(summary = "新增租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "新增套餐", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "user-mod:tenantpackage-mng:add")
    public Result<Void> save(@Valid @RequestBody TenantPackageMngSaveRequest request) {
        return tenantPackageMngApi.save(request);
    }

    @Operation(summary = "修改租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "修改套餐", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "user-mod:tenantpackage-mng:edit")
    public Result<Void> update(@Valid @RequestBody TenantPackageMngUpdateRequest request) {
        return tenantPackageMngApi.update(request);
    }

    @Operation(summary = "删除租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "删除套餐", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "user-mod:tenantpackage-mng:remove")
    public Result<Void> remove(@RequestBody @Valid IdListRequest request) {
        return tenantPackageMngApi.remove(request);
    }

}

