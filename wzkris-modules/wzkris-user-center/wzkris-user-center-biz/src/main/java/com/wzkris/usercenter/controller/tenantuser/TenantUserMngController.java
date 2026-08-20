package com.wzkris.usercenter.controller.tenantuser;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.usercenter.api.tenantuser.TenantUserMngApi;
import com.wzkris.usercenter.api.tenantuser.request.*;
import com.wzkris.usercenter.api.tenantuser.response.TenantUserMngPageResponse;
import com.wzkris.usercenter.api.tenantuser.response.TenantUserMngQueryResponse;
import com.wzkris.usercenter.request.PwdResetRequest;
import com.wzkris.usercenter.response.CheckedSelectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "租户用户管理")
@Validated
@RestController
@RequestMapping("/tenant-user-manage")
@RequiredArgsConstructor
public class TenantUserMngController {

    private static final String PERM_PREFIX = "user-mod:tenant-user-mng:";

    private final TenantUserMngApi tenantUserMngApi;

    @Operation(summary = "分页列表")
    @GetMapping("/query-page")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX, value = "page")
    public Result<Page<TenantUserMngPageResponse>> queryPage(@ParameterObject TenantUserMngPageRequest request) {
        return tenantUserMngApi.queryPage(request);
    }

    @Operation(summary = "用户详细信息")
    @GetMapping("/query-id/{id}")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX, value = "page")
    public Result<TenantUserMngQueryResponse> queryById(@ParameterObject IdRequest request) {
        return tenantUserMngApi.queryById(request);
    }

    @Operation(summary = "用户-角色选择列表")
    @GetMapping("/query-role-checked-select")
    @CheckPerms(
            checkTypes = AuthTypeEnum.TENANT,
            prefix = PERM_PREFIX, value = {"edit", "add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectResponse> queryRoleSelect(@ParameterObject TenantUserMngRoleSelectRequest request) {
        return tenantUserMngApi.queryRoleSelect(request);
    }

    @Operation(summary = "新增用户")
    @OperateLog(title = "用户管理", subTitle = "新增用户", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX, value = "add")
    public Result<Void> save(@Validated @RequestBody TenantUserMngSaveRequest request) {
        return tenantUserMngApi.save(request);
    }

    @Operation(summary = "修改用户")
    @OperateLog(title = "用户管理", subTitle = "修改用户", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX, value = "edit")
    public Result<Void> update(@Validated @RequestBody TenantUserMngUpdateRequest request) {
        return tenantUserMngApi.update(request);
    }

    @Operation(summary = "重置密码")
    @OperateLog(title = "用户管理", subTitle = "重置密码", type = OperateTypeEnum.UPDATE)
    @PostMapping("/reset-password")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX, value = "edit")
    public Result<Void> resetPwd(@RequestBody @Valid PwdResetRequest request) {
        return tenantUserMngApi.resetPwd(request);
    }

    @Operation(summary = "授权角色")
    @OperateLog(title = "用户管理", subTitle = "授权用户角色", type = OperateTypeEnum.GRANT)
    @PostMapping("/grant-role")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX, value = "grant-role")
    public Result<Void> grantRoles(@RequestBody @Valid TenantUserMngGrantRoleRequest request) {
        return tenantUserMngApi.grantRoles(request);
    }

    @Operation(summary = "删除用户")
    @OperateLog(title = "用户管理", subTitle = "删除用户", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX, value = "remove")
    public Result<Void> remove(@RequestBody @Valid IdListRequest request) {
        return tenantUserMngApi.remove(request);
    }

}

