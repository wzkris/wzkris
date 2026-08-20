package com.wzkris.usercenter.controller.tenantrole;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.usercenter.api.tenantrole.TenantRoleMngApi;
import com.wzkris.usercenter.api.tenantrole.request.TenantRoleMngPageRequest;
import com.wzkris.usercenter.api.tenantrole.request.TenantRoleMngSaveRequest;
import com.wzkris.usercenter.api.tenantrole.request.TenantRoleMngUpdateRequest;
import com.wzkris.usercenter.api.tenantrole.response.TenantRoleMngPageResponse;
import com.wzkris.usercenter.api.tenantrole.response.TenantRoleMngQueryResponse;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "租户角色管理")
@Validated
@RestController
@RequestMapping("/tenant-role-manage")
@RequiredArgsConstructor
public class TenantRoleMngController {

    private static final String PERM_PREFIX = "user-mod:tenant-role-mng:";

    private final TenantRoleMngApi roleMngApi;

    @Operation(summary = "角色分页")
    @GetMapping("/query-page")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX, value = "page")
    public Result<Page<TenantRoleMngPageResponse>> queryPage(@ParameterObject TenantRoleMngPageRequest request) {
        return roleMngApi.queryPage(request);
    }

    @Operation(summary = "角色详细信息")
    @GetMapping("/query-id/{id}")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX, value = "page")
    public Result<TenantRoleMngQueryResponse> queryById(@ParameterObject IdRequest request) {
        return roleMngApi.queryById(request);
    }

    @Operation(summary = "角色-菜单选择树")
    @GetMapping({"/query-menu-checked-selecttree/", "/query-menu-checked-selecttree/{id}"})
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX,
            value = {"edit", "add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectTreeResponse> queryMenuSelectTree(@ParameterObject IdRequest request) {
        return roleMngApi.queryMenuSelectTree(request);
    }

    @Operation(summary = "新增角色")
    @OperateLog(title = "角色管理", subTitle = "新增角色", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX, value = "add")
    public Result<Void> save(@Validated @RequestBody TenantRoleMngSaveRequest request) {
        return roleMngApi.save(request);
    }

    @Operation(summary = "修改角色")
    @OperateLog(title = "角色管理", subTitle = "修改角色", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX, value = "edit")
    public Result<Void> update(@Validated @RequestBody TenantRoleMngUpdateRequest request) {
        return roleMngApi.update(request);
    }

    @Operation(summary = "删除角色")
    @OperateLog(title = "角色管理", subTitle = "删除角色", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX, value = "remove")
    public Result<Void> remove(@RequestBody @Valid IdListRequest request) {
        return roleMngApi.remove(request);
    }

}

