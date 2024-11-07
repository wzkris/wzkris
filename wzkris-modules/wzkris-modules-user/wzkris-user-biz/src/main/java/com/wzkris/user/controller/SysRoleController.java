package com.wzkris.user.controller;

import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.page.Page;
import com.wzkris.user.domain.*;
import com.wzkris.user.domain.dto.SysRoleDTO;
import com.wzkris.user.domain.dto.SysRoleUsersDTO;
import com.wzkris.user.mapper.SysRoleDeptMapper;
import com.wzkris.user.mapper.SysRoleMapper;
import com.wzkris.user.mapper.SysRoleMenuMapper;
import com.wzkris.user.mapper.SysUserRoleMapper;
import com.wzkris.user.service.SysDeptService;
import com.wzkris.user.service.SysMenuService;
import com.wzkris.user.service.SysRoleService;
import com.wzkris.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色信息
 *
 * @author wzkris
 */
@Tag(name = "角色管理")
@Validated
@RestController
@RequestMapping("/sys_role")
@RequiredArgsConstructor
public class SysRoleController extends BaseController {
    private final SysRoleMapper roleMapper;
    private final SysRoleService roleService;
    private final SysUserService userService;
    private final SysUserRoleMapper userRoleMapper;
    private final SysRoleDeptMapper sysRoleDeptMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysDeptService deptService;
    private final SysMenuService menuService;

    @Operation(summary = "角色分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('sys_role:list')")
    public Result<Page<SysRole>> listPage(SysRole role) {
        startPage();
        List<SysRole> list = roleService.list(role);
        return getDataTable(list);
    }

    @Operation(summary = "角色详细信息")
    @GetMapping("/{roleId}")
    @PreAuthorize("@ps.hasPerms('sys_role:query')")
    public Result<?> getInfo(@PathVariable Long roleId) {
        // 权限校验
        roleService.checkDataScopes(roleId);
        return ok(roleMapper.selectById(roleId));
    }

    @Operation(summary = "角色菜单选择树")
    @GetMapping({"/menu_select_tree/", "/menu_select_tree/{roleId}"})
    @PreAuthorize("@ps.hasPerms('sys_role:list')")
    public Result<?> roleMenuTreeList(@PathVariable(required = false) Long roleId) {
        Map<String, Object> res = new HashMap<>(2);
        res.put("checkedKeys", roleId == null ? Collections.emptyList() :
                sysRoleMenuMapper.listMenuIdByRoleIds(Collections.singletonList(roleId)));
        res.put("menus", menuService.listMenuSelectTree(new SysMenu(CommonConstants.STATUS_ENABLE)));
        return ok(res);
    }

    @Operation(summary = "新增角色")
    @OperateLog(title = "角色管理", subTitle = "新增角色", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('sys_role:add')")
    public Result<?> add(@Validated @RequestBody SysRoleDTO roleDTO) {
        return toRes(roleService.insertRole(roleDTO));
    }

    @Operation(summary = "修改角色")
    @OperateLog(title = "角色管理", subTitle = "修改角色", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('sys_role:edit')")
    public Result<?> edit(@Validated @RequestBody SysRoleDTO roleDTO) {
        // 权限校验
        roleService.checkDataScopes(roleDTO.getRoleId());
        return toRes(roleService.updateRole(roleDTO));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "后台管理", subTitle = "状态修改", operateType = OperateType.UPDATE)
    @PostMapping("/edit_status")
    @PreAuthorize("@ps.hasPerms('user:edit')")
    public Result<?> editStatus(@RequestBody SysRole role) {
        // 校验权限
        roleService.checkDataScopes(role.getRoleId());
        SysRole update = new SysRole(role.getRoleId());
        update.setStatus(role.getStatus());
        return toRes(roleMapper.updateById(update));
    }

    @Operation(summary = "修改角色数据权限")
    @OperateLog(title = "角色管理", subTitle = "修改角色数据权限", operateType = OperateType.UPDATE)
    @PostMapping("/data_scope")
    @PreAuthorize("@ps.hasPerms('sys_role:dataScope')")
    public Result<?> dataScope(@RequestBody SysRoleDTO roleDTO) {
        // 权限校验
        roleService.checkDataScopes(roleDTO.getRoleId());
        return toRes(roleService.updateDeptScope(roleDTO));
    }

    @Operation(summary = "删除角色")
    @OperateLog(title = "角色管理", subTitle = "删除角色", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('sys_role:remove')")
    public Result<?> remove(@RequestBody @NotEmpty(message = "[roleIds] {validate.notnull}") List<Long> roleIds) {
        // 权限校验
        roleService.checkDataScopes(roleIds);
        roleService.checkUserUse(roleIds);
        return toRes(roleService.deleteByIds(roleIds));
    }

    @Operation(summary = "查询已授权的用户列表")
    @GetMapping("/authorize/allocated_list")
    @PreAuthorize("@ps.hasPerms('sys_role:list')")
    public Result<Page<SysUser>> allocatedList(SysUser user, Long roleId) {
        startPage();
        List<SysUser> list = userService.listAllocated(user, roleId);
        return getDataTable(list);
    }

    @Operation(summary = "查询未授权的用户列表")
    @GetMapping("/authorize/unallocated_list")
    @PreAuthorize("@ps.hasPerms('sys_role:list')")
    public Result<Page<SysUser>> unallocatedList(SysUser user, Long roleId) {
        startPage();
        List<SysUser> list = userService.listUnallocated(user, roleId);
        return getDataTable(list);
    }

    @Operation(summary = "取消授权")
    @OperateLog(title = "角色管理", subTitle = "取消授权", operateType = OperateType.GRANT)
    @PostMapping("/authorize/cancel")
    @PreAuthorize("@ps.hasPerms('sys_role:auth')")
    public Result<?> cancelAuth(@RequestBody @Valid SysUserRole userRole) {
        // 校验角色权限
        roleService.checkDataScopes(userRole.getRoleId());
        // 校验用户权限
        userService.checkDataScopes(userRole.getUserId());
        return toRes(userRoleMapper.delete(userRole.getUserId(), userRole.getRoleId()));
    }

    @Operation(summary = "批量取消授权")
    @OperateLog(title = "角色管理", subTitle = "批量取消授权", operateType = OperateType.GRANT)
    @PostMapping("/authorize/cancel_batch")
    @PreAuthorize("@ps.hasPerms('sys_role:auth')")
    public Result<?> cancelAuth(@RequestBody @Valid SysRoleUsersDTO roleUsersDTO) {
        // 权限校验
        roleService.checkDataScopes(roleUsersDTO.getRoleId());
        // 校验用户权限
        userService.checkDataScopes(roleUsersDTO.getUserIds());
        return toRes(userRoleMapper.deleteBatch(roleUsersDTO.getRoleId(), roleUsersDTO.getUserIds()));
    }

    @Operation(summary = "批量用户授权")
    @OperateLog(title = "角色管理", subTitle = "批量用户授权", operateType = OperateType.GRANT)
    @PostMapping("/authorize_user")
    @PreAuthorize("@ps.hasPerms('sys_role:auth')")
    public Result<?> batchAuth(@RequestBody @Valid SysRoleUsersDTO roleUsersDTO) {
        // 权限校验
        roleService.checkDataScopes(roleUsersDTO.getRoleId());
        roleService.allocateUsers(roleUsersDTO.getRoleId(), roleUsersDTO.getUserIds());
        return ok();
    }

    @Operation(summary = "角色部门树列表")
    @GetMapping("/dept_tree/{roleId}")
    @PreAuthorize("@ps.hasPerms('sys_role:query')")
    public Result<?> deptTree(@PathVariable Long roleId) {
        // 权限校验
        roleService.checkDataScopes(roleId);
        Map<String, Object> res = new HashMap<>(2);
        res.put("checkedKeys", sysRoleDeptMapper.listDeptIdByRoleId(roleId));
        res.put("depts", deptService.listDeptTree(new SysDept()));
        return ok(res);
    }
}
