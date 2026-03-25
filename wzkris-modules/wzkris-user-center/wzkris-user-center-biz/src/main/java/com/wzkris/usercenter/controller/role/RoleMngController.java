package com.wzkris.usercenter.controller.role;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.usercenter.api.role.RoleMngApi;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.role.RoleMngQueryRequest;
import com.wzkris.usercenter.request.role.RoleMngSaveRequest;
import com.wzkris.usercenter.request.role.RoleMngUpdateRequest;
import com.wzkris.usercenter.response.CheckedSelectResponse;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;
import com.wzkris.usercenter.response.role.RoleInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色信息
 *
 * @author wzkris
 */
@Tag(name = "角色管理")
@Validated
@RestController
@RequestMapping("/role-manage")
@RequiredArgsConstructor
public class RoleMngController extends BaseController {

    private final RoleMngApi roleMngApi;

    @Operation(summary = "角色分页")
    @GetMapping("/query-page")
    @CheckAdminPerms("user-mod:role-mng:page")
    public Result<Page<RoleInfoResponse>> queryPage(RoleMngQueryRequest request) {
        return roleMngApi.queryPage(request);
    }

    @Operation(summary = "角色详细信息")
    @GetMapping("/query-info/{roleId}")
    @CheckAdminPerms("user-mod:role-mng:query")
    public Result<RoleInfoResponse> queryInfo(@PathVariable Long roleId) {
        return roleMngApi.queryInfo(roleId);
    }

    @Operation(summary = "角色 - 菜单选择树")
    @GetMapping({"/query-menu-checked-selecttree/", "/query-menu-checked-selecttree/{roleId}"})
    @CheckAdminPerms(
            value = {"user-mod:role-mng:edit", "user-mod:role-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectTreeResponse> queryRoleMenuSelectTree(@PathVariable(required = false) Long roleId) {
        return roleMngApi.queryRoleMenuSelectTree(roleId);
    }

    @Operation(summary = "角色 - 部门选择树")
    @GetMapping({"/query-dept-checked-selecttree/", "/query-dept-checked-selecttree/{roleId}"})
    @CheckAdminPerms(
            value = {"user-mod:role-mng:edit", "user-mod:role-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectTreeResponse> queryRoleDeptSelectTree(@PathVariable(required = false) Long roleId) {
        return roleMngApi.queryRoleDeptSelectTree(roleId);
    }

    @Operation(summary = "角色 - 继承选择列表")
    @GetMapping({"/query-hierarchy-checked-select/", "/query-hierarchy-checked-select/{roleId}"})
    @CheckAdminPerms(
            value = {"user-mod:role-mng:edit", "user-mod:role-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectResponse> queryRoleInheritedSelect(@PathVariable(required = false) Long roleId) {
        return roleMngApi.queryRoleInheritedSelect(roleId);
    }

    @Operation(summary = "新增角色")
    @OperateLog(title = "角色管理", subTitle = "新增角色", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckAdminPerms("user-mod:role-mng:add")
    public Result<Void> save(@Validated @RequestBody RoleMngSaveRequest request) {
        return roleMngApi.save(request);
    }

    @Operation(summary = "修改角色")
    @OperateLog(title = "角色管理", subTitle = "修改角色", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckAdminPerms("user-mod:role-mng:edit")
    public Result<Void> update(@Validated @RequestBody RoleMngUpdateRequest request) {
        return roleMngApi.update(request);
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "用户管理", subTitle = "状态修改", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-status")
    @CheckAdminPerms("user-mod:role-mng:edit")
    public Result<Void> updateStatus(@RequestBody StatusUpdateRequest request) {
        return roleMngApi.updateStatus(request);
    }

    @Operation(summary = "删除角色")
    @OperateLog(title = "角色管理", subTitle = "删除角色", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("user-mod:role-mng:remove")
    public Result<Void> remove(@RequestBody @NotEmpty(message = "{invalidParameter.id.invalid}") List<Long> roleIds) {
        return roleMngApi.remove(roleIds);
    }

}

