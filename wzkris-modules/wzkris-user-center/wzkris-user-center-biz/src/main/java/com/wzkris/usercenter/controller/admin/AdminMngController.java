package com.wzkris.usercenter.controller.admin;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.usercenter.api.admin.AdminMngApi;
import com.wzkris.usercenter.api.admin.request.*;
import com.wzkris.usercenter.api.admin.response.AdminMngQueryResponse;
import com.wzkris.usercenter.api.admin.response.AdminMngPageResponse;
import com.wzkris.usercenter.request.PwdResetRequest;
import com.wzkris.usercenter.response.CheckedSelectResponse;
import com.wzkris.usercenter.response.SelectTreeResponse;
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

    private static final String PERM_PREFIX = "user-mod:admin-mng:";

    private final AdminMngApi adminMngApi;

    @Operation(summary = "管理员分页列表")
    @GetMapping("/query-page")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "page")
    public Result<Page<AdminMngPageResponse>> queryPage(@ParameterObject AdminMngPageRequest request) {
        return adminMngApi.queryPage(request);
    }

    @Operation(summary = "管理员 - 部门选择树")
    @GetMapping("/query-dept-selecttree")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX,
            value = {"edit", "add"},
            mode = CheckMode.OR)
    public Result<List<SelectTreeResponse>> queryDeptSelectTree(@ParameterObject AdminMngDeptSelectRequest request) {
        return adminMngApi.queryDeptSelectTree(request);
    }

    @Operation(summary = "管理员 - 角色选择列表")
    @GetMapping("/query-role-checked-select")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX,
            value = {"edit", "add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectResponse> queryRoleSelect(@ParameterObject AdminMngRoleSelectRequest request) {
        return adminMngApi.queryRoleSelect(request);
    }

    @Operation(summary = "管理员详细信息")
    @GetMapping("/query-id/{id}")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "query")
    public Result<AdminMngQueryResponse> queryById(@ParameterObject IdRequest request) {
        return adminMngApi.queryById(request);
    }

    @Operation(summary = "新增管理员")
    @OperateLog(title = "管理员管理", subTitle = "新增管理员", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "add")
    public Result<Void> save(@Validated @RequestBody AdminMngSaveRequest request) {
        return adminMngApi.save(request);
    }

    @Operation(summary = "修改管理员")
    @OperateLog(title = "管理员管理", subTitle = "修改管理员", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "edit")
    public Result<Void> update(@Validated @RequestBody AdminMngUpdateRequest request) {
        return adminMngApi.update(request);
    }

    @Operation(summary = "管理员授权角色")
    @OperateLog(title = "管理员管理", subTitle = "授权管理员角色", type = OperateTypeEnum.GRANT)
    @PostMapping("/grant-role")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "grant-role")
    public Result<Void> grantRoles(@RequestBody @Valid AdminMngGrantRequest request) {
        return adminMngApi.grantRoles(request);
    }

    @Operation(summary = "删除管理员")
    @OperateLog(title = "管理员管理", subTitle = "删除管理员", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "remove")
    public Result<Void> remove(@RequestBody @Valid IdListRequest request) {
        return adminMngApi.remove(request);
    }

    @Operation(summary = "重置密码")
    @OperateLog(title = "管理员管理", subTitle = "重置密码", type = OperateTypeEnum.UPDATE)
    @PostMapping("/reset-password")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = PERM_PREFIX + "edit")
    public Result<Void> resetPwd(@RequestBody @Valid PwdResetRequest request) {
        return adminMngApi.resetPwd(request);
    }

    @Operation(summary = "导出")
    @OperateLog(title = "管理员管理", subTitle = "导出管理员数据", type = OperateTypeEnum.EXPORT_IMPORT)
    @GetMapping("/export")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = PERM_PREFIX + "export")
    public void export(HttpServletResponse response, @ParameterObject AdminMngPageRequest request) {
        adminMngApi.export(response, request);
    }

}

