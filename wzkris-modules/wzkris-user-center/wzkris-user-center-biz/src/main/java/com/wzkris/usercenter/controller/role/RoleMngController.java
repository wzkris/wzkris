package com.wzkris.usercenter.controller.role;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.usercenter.api.role.RoleMngApi;
import com.wzkris.usercenter.api.role.request.RoleMngPageRequest;
import com.wzkris.usercenter.api.role.request.RoleMngSaveRequest;
import com.wzkris.usercenter.api.role.request.RoleMngUpdateRequest;
import com.wzkris.usercenter.api.role.response.RoleMngResponse;
import com.wzkris.usercenter.response.CheckedSelectResponse;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "角色管理")
@Validated
@RestController
@RequestMapping("/role-manage")
@RequiredArgsConstructor
public class RoleMngController {

    private final RoleMngApi roleMngApi;

    @Operation(summary = "角色分页")
    @GetMapping("/query-page")
    @CheckAdminPerms("user-mod:role-mng:page")
    public Result<Page<RoleMngResponse>> queryPage(@ParameterObject RoleMngPageRequest request) {
        return roleMngApi.queryPage(request);
    }

    @Operation(summary = "角色详细信息")
    @GetMapping("/query-info/{id}")
    @CheckAdminPerms("user-mod:role-mng:query")
    public Result<RoleMngResponse> queryInfo(@ParameterObject IdRequest request) {
        return roleMngApi.queryInfo(request);
    }

    @Operation(summary = "角色 - 菜单选择树")
    @GetMapping({"/query-menu-checked-selecttree/", "/query-menu-checked-selecttree/{id}"})
    @CheckAdminPerms(
            value = {"user-mod:role-mng:edit", "user-mod:role-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectTreeResponse> queryRoleMenuSelectTree(@ParameterObject IdRequest request) {
        return roleMngApi.queryRoleMenuSelectTree(request);
    }

    @Operation(summary = "角色 - 部门选择树")
    @GetMapping({"/query-dept-checked-selecttree/", "/query-dept-checked-selecttree/{id}"})
    @CheckAdminPerms(
            value = {"user-mod:role-mng:edit", "user-mod:role-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectTreeResponse> queryRoleDeptSelectTree(@ParameterObject IdRequest request) {
        return roleMngApi.queryRoleDeptSelectTree(request);
    }

    @Operation(summary = "角色 - 继承选择列表")
    @GetMapping({"/query-hierarchy-checked-select/", "/query-hierarchy-checked-select/{id}"})
    @CheckAdminPerms(
            value = {"user-mod:role-mng:edit", "user-mod:role-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectResponse> queryRoleInheritedSelect(@ParameterObject IdRequest request) {
        return roleMngApi.queryRoleInheritedSelect(request);
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

    @Operation(summary = "删除角色")
    @OperateLog(title = "角色管理", subTitle = "删除角色", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("user-mod:role-mng:remove")
    public Result<Void> remove(@RequestBody @Valid IdListRequest request) {
        return roleMngApi.remove(request);
    }

}

