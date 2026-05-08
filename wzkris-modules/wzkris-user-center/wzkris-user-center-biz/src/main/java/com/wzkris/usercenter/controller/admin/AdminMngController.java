package com.wzkris.usercenter.controller.admin;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.usercenter.api.admin.AdminMngApi;
import com.wzkris.usercenter.request.admin.*;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.request.common.PwdResetRequest;
import com.wzkris.usercenter.request.dept.DeptMngListRequest;
import com.wzkris.usercenter.response.admin.AdminMngResponse;
import com.wzkris.usercenter.response.common.CheckedSelectResponse;
import com.wzkris.usercenter.response.common.SelectTreeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "管理员管理")
@Validated
@RestController
@RequestMapping("/admin-manage")
@RequiredArgsConstructor
public class AdminMngController {

    private final AdminMngApi adminMngApi;

    @Operation(summary = "管理员分页列表")
    @GetMapping("/query-page")
    @CheckAdminPerms("user-mod:admin-mng:page")
    public Result<Page<AdminMngResponse>> queryPage(@ParameterObject AdminMngPageRequest request) {
        return adminMngApi.queryPage(request);
    }

    @Operation(summary = "管理员 - 部门选择树")
    @GetMapping("/query-dept-selecttree")
    @CheckAdminPerms(
            value = {"user-mod:admin-mng:edit", "user-mod:admin-mng:add"},
            mode = CheckMode.OR)
    public Result<List<SelectTreeResponse>> queryDeptSelectTree(@ParameterObject DeptMngListRequest request) {
        return adminMngApi.queryDeptSelectTree(request);
    }

    @Operation(summary = "管理员 - 角色选择列表")
    @GetMapping("/query-role-checked-select")
    @CheckAdminPerms(
            value = {"user-mod:admin-mng:edit", "user-mod:admin-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectResponse> queryRoleSelect(@ParameterObject AdminMngRoleSelectRequest request) {
        return adminMngApi.queryRoleSelect(request);
    }

    @Operation(summary = "管理员详细信息")
    @GetMapping("/query-info/{id}")
    @CheckAdminPerms("user-mod:admin-mng:query")
    public Result<AdminMngResponse> queryInfo(@ParameterObject IdRequest request) {
        return adminMngApi.queryInfo(request);
    }

    @Operation(summary = "新增管理员")
    @OperateLog(title = "管理员管理", subTitle = "新增管理员", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckAdminPerms("user-mod:admin-mng:add")
    public Result<Void> save(@Validated @RequestBody AdminMngSaveRequest request) {
        return adminMngApi.save(request);
    }

    @Operation(summary = "修改管理员")
    @OperateLog(title = "管理员管理", subTitle = "修改管理员", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckAdminPerms("user-mod:admin-mng:edit")
    public Result<Void> update(@Validated @RequestBody AdminMngUpdateRequest request) {
        return adminMngApi.update(request);
    }

    @Operation(summary = "管理员授权角色")
    @OperateLog(title = "管理员管理", subTitle = "授权管理员角色", type = OperateTypeEnum.GRANT)
    @PostMapping("/grant-role")
    @CheckAdminPerms("user-mod:admin-mng:grant-role")
    public Result<Void> grantRoles(@RequestBody @Valid AdminMngGrantRequest request) {
        return adminMngApi.grantRoles(request);
    }

    @Operation(summary = "删除管理员")
    @OperateLog(title = "管理员管理", subTitle = "删除管理员", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("user-mod:admin-mng:remove")
    public Result<Void> remove(@RequestBody @Valid IdListRequest request) {
        return adminMngApi.remove(request);
    }

    @Operation(summary = "重置密码")
    @OperateLog(title = "管理员管理", subTitle = "重置密码", type = OperateTypeEnum.UPDATE)
    @PostMapping("/reset-password")
    @CheckAdminPerms("user-mod:admin-mng:edit")
    public Result<Void> resetPwd(@RequestBody @Valid PwdResetRequest request) {
        return adminMngApi.resetPwd(request);
    }

    @Operation(summary = "导出")
    @OperateLog(title = "管理员管理", subTitle = "导出管理员数据", type = OperateTypeEnum.EXPORT)
    @GetMapping("/export")
    @CheckAdminPerms("user-mod:admin-mng:export")
    public void export(HttpServletResponse response, @ParameterObject AdminMngPageRequest request) {
        adminMngApi.export(response, request);
    }

}

