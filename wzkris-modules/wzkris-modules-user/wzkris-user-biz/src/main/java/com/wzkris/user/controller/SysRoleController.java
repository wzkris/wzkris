package com.wzkris.user.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.security.utils.SysUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.SysRole;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.SysUserRole;
import com.wzkris.user.domain.dto.SysRoleDTO;
import com.wzkris.user.domain.req.EditStatusReq;
import com.wzkris.user.domain.req.SysRole2UsersReq;
import com.wzkris.user.domain.req.SysRoleQueryReq;
import com.wzkris.user.domain.vo.SysDeptCheckSelectTreeVO;
import com.wzkris.user.domain.vo.SysMenuCheckSelectTreeVO;
import com.wzkris.user.mapper.SysRoleDeptMapper;
import com.wzkris.user.mapper.SysRoleMapper;
import com.wzkris.user.mapper.SysRoleMenuMapper;
import com.wzkris.user.mapper.SysUserRoleMapper;
import com.wzkris.user.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

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
    private final SysRoleDeptMapper roleDeptMapper;
    private final SysRoleMenuMapper roleMenuMapper;
    private final SysDeptService deptService;
    private final SysMenuService menuService;
    private final SysTenantService tenantService;

    @Operation(summary = "角色分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('sys_role:list')")
    public Result<Page<SysRole>> listPage(SysRoleQueryReq req) {
        startPage();
        List<SysRole> list = roleMapper.selectListInScope(this.buildQueryWrapper(req));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<SysRole> buildQueryWrapper(SysRoleQueryReq req) {
        return new LambdaQueryWrapper<SysRole>()
                .like(StringUtil.isNotNull(req.getRoleName()), SysRole::getRoleName, req.getRoleName())
                .eq(StringUtil.isNotNull(req.getStatus()), SysRole::getStatus, req.getStatus())
                .orderByDesc(SysRole::getRoleSort, SysRole::getRoleId);
    }

    @Operation(summary = "角色详细信息")
    @GetMapping("/{roleId}")
    @PreAuthorize("@ps.hasPerms('sys_role:query')")
    public Result<SysRole> getInfo(@PathVariable Long roleId) {
        // 权限校验
        roleService.checkDataScopes(roleId);
        return ok(roleMapper.selectById(roleId));
    }

    @Operation(summary = "角色菜单选择树")
    @GetMapping({"/menu_select_tree/", "/menu_select_tree/{roleId}"})
    @PreAuthorize("@ps.hasPerms('sys_role:list')")
    public Result<SysMenuCheckSelectTreeVO> roleMenuTreeList(@PathVariable(required = false) Long roleId) {
        // 权限校验
        roleService.checkDataScopes(roleId);
        SysMenuCheckSelectTreeVO resp = new SysMenuCheckSelectTreeVO();
        resp.setCheckedKeys(roleId == null ? Collections.emptyList() : roleMenuMapper.listMenuIdByRoleIds(Collections.singletonList(roleId)));
        resp.setMenus(menuService.listMenuSelectTree(SysUtil.getUserId()));
        return ok(resp);
    }

    @Operation(summary = "角色部门选择树")
    @GetMapping({"/dept_select_tree/{roleId}", "/dept_select_tree/{roleId}"})
    @PreAuthorize("@ps.hasPerms('sys_role:query')")
    public Result<SysDeptCheckSelectTreeVO> deptTree(@PathVariable(required = false) Long roleId) {
        // 权限校验
        roleService.checkDataScopes(roleId);
        SysDeptCheckSelectTreeVO resp = new SysDeptCheckSelectTreeVO();
        resp.setCheckedKeys(roleId == null ? Collections.emptyList() : roleDeptMapper.listDeptIdByRoleId(roleId));
        resp.setDepts(deptService.listSelectTree(null));
        return ok(resp);
    }

    @Operation(summary = "新增角色")
    @OperateLog(title = "角色管理", subTitle = "新增角色", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('sys_role:add')")
    public Result<Void> add(@Validated @RequestBody SysRoleDTO roleDTO) {
        if (!tenantService.checkRoleLimit(SysUtil.getTenantId())) {
            return fail("角色数量已达上限，请联系管理员");
        }
        roleService.insertRole(roleDTO);
        return ok();
    }

    @Operation(summary = "修改角色")
    @OperateLog(title = "角色管理", subTitle = "修改角色", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('sys_role:edit')")
    public Result<Void> edit(@Validated @RequestBody SysRoleDTO roleDTO) {
        // 权限校验
        roleService.checkDataScopes(roleDTO.getRoleId());
        roleService.updateRole(roleDTO);
        return ok();
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "后台管理", subTitle = "状态修改", operateType = OperateType.UPDATE)
    @PostMapping("/edit_status")
    @PreAuthorize("@ps.hasPerms('user:edit')")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        // 校验权限
        roleService.checkDataScopes(statusReq.getId());
        SysRole update = new SysRole(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(roleMapper.updateById(update));
    }

    @Operation(summary = "删除角色")
    @OperateLog(title = "角色管理", subTitle = "删除角色", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('sys_role:remove')")
    public Result<Void> remove(@RequestBody @NotEmpty(message = "[roleIds] {validate.notnull}") List<Long> roleIds) {
        // 权限校验
        roleService.checkDataScopes(roleIds);
        roleService.checkRoleUse(roleIds);
        roleService.deleteByIds(roleIds);
        return ok();
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
    public Result<Void> cancelAuth(@RequestBody @Valid SysUserRole userRole) {
        if (ObjUtil.equals(userRole.getUserId(), SysUtil.getUserId())) {
            return fail("不能对自己解除授权");
        }
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
    public Result<Void> cancelAuth(@RequestBody @Valid SysRole2UsersReq req) {
        if (CollUtil.contains(req.getUserIds(), SysUtil.getUserId())) {
            return fail("不能对自己解除授权");
        }
        // 权限校验
        roleService.checkDataScopes(req.getRoleId());
        // 校验用户权限
        userService.checkDataScopes(req.getUserIds());
        return toRes(userRoleMapper.deleteBatch(req.getRoleId(), req.getUserIds()));
    }

    @Operation(summary = "批量用户授权")
    @OperateLog(title = "角色管理", subTitle = "批量用户授权", operateType = OperateType.GRANT)
    @PostMapping("/authorize_user")
    @PreAuthorize("@ps.hasPerms('sys_role:auth')")
    public Result<Void> batchAuth(@RequestBody @Valid SysRole2UsersReq req) {
        if (CollUtil.contains(req.getUserIds(), SysUtil.getUserId())) {
            return fail("不能授权自己");
        }
        // 权限校验
        roleService.checkDataScopes(req.getRoleId());
        // 校验用户权限
        userService.checkDataScopes(req.getUserIds());
        roleService.allocateUsers(req.getRoleId(), req.getUserIds());
        return ok();
    }

}
