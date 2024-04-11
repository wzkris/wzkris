package com.thingslink.auth.controller;

import cn.hutool.core.util.ObjUtil;
import com.thingslink.auth.domain.SysDept;
import com.thingslink.auth.domain.SysRole;
import com.thingslink.auth.domain.SysUser;
import com.thingslink.auth.domain.SysUserRole;
import com.thingslink.auth.domain.dto.SysRoleDTO;
import com.thingslink.auth.mapper.SysRoleDeptMapper;
import com.thingslink.auth.mapper.SysRoleMapper;
import com.thingslink.auth.mapper.SysUserRoleMapper;
import com.thingslink.auth.service.SysDeptService;
import com.thingslink.auth.service.SysRoleService;
import com.thingslink.auth.service.SysUserService;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.BusinessType;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色信息
 *
 * @author wzkris
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class SysRoleController extends BaseController {
    private final SysRoleMapper sysRoleMapper;
    private final SysRoleService sysRoleService;
    private final SysUserService userService;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleDeptMapper sysRoleDeptMapper;
    private final SysDeptService sysDeptService;

    @Operation(summary = "角色分页")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('role:list')")
    public Result<Page<SysRole>> list(SysRole role) {
        startPage();
        List<SysRole> list = sysRoleService.list(role);
        return getDataTable(list);
    }

    @Operation(summary = "角色详细信息")
    @GetMapping("/{roleId}")
    @PreAuthorize("hasAuthority('role:query')")
    public Result<?> getInfo(@PathVariable Long roleId) {
        // 权限校验
        sysRoleService.checkDataScopes(roleId);
        return success(sysRoleMapper.selectById(roleId));
    }

    @Operation(summary = "新增角色")
    @OperateLog(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    @PreAuthorize("hasAuthority('role:add')")
    public Result<?> add(@Validated @RequestBody SysRoleDTO roleDTO) {
        // 判断key是否重复
        if (ObjUtil.isNotNull(roleDTO.getRoleKey()) &&
                sysRoleService.checkRoleKeyUnique(roleDTO.getRoleKey(), roleDTO.getRoleId())) {
            return fail("新增角色'" + roleDTO.getRoleKey() + "'失败，角色权限已存在");
        }
        return toRes(sysRoleService.insertRole(roleDTO));
    }

    @Operation(summary = "修改角色")
    @OperateLog(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @PreAuthorize("hasAuthority('role:edit')")
    public Result<?> edit(@Validated @RequestBody SysRoleDTO roleDTO) {
        // 权限校验
        sysRoleService.checkDataScopes(roleDTO.getRoleId());
        if (ObjUtil.isNotNull(roleDTO.getRoleKey()) &&
                sysRoleService.checkRoleKeyUnique(roleDTO.getRoleKey(), roleDTO.getRoleId())) {
            return fail("新增角色'" + roleDTO.getRoleKey() + "'失败，角色权限已存在");
        }
        return toRes(sysRoleService.updateRole(roleDTO));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "后台管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    @PreAuthorize("hasAuthority('user:edit')")
    public Result<?> changeStatus(@RequestBody SysRole role) {
        // 校验权限
        sysRoleService.checkDataScopes(role.getRoleId());
        SysRole update = new SysRole(role.getRoleId());
        update.setStatus(role.getStatus());
        return toRes(sysRoleMapper.updateById(update));
    }

    @Operation(summary = "修改角色数据权限")
    @OperateLog(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    @PreAuthorize("hasAuthority('role:dataScope')")
    public Result<?> dataScope(@RequestBody SysRoleDTO roleDTO) {
        // 权限校验
        sysRoleService.checkDataScopes(roleDTO.getRoleId());
        return toRes(sysRoleService.authDeptScope(roleDTO));
    }

    @Operation(summary = "删除角色")
    @OperateLog(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    @PreAuthorize("hasAuthority('role:remove')")
    public Result<?> remove(@PathVariable Long[] roleIds) {
        // 权限校验
        sysRoleService.checkDataScopes(roleIds);
        sysRoleService.checkUserUse(roleIds);
        return toRes(sysRoleService.deleteBatchByIds(roleIds));
    }

    @Operation(summary = "查询角色已分配的用户列表")
    @GetMapping("/authUser/allocatedList")
    @PreAuthorize("hasAuthority('role:list')")
    public Result<Page<SysUser>> allocatedList(SysUser user, Long roleId) {
        startPage();
        List<SysUser> list = userService.listAllocated(user, roleId);
        return getDataTable(list);
    }

    @Operation(summary = "查询角色未分配的用户列表")
    @GetMapping("/authUser/unallocatedList")
    @PreAuthorize("hasAuthority('role:list')")
    public Result<Page<SysUser>> unallocatedList(SysUser user, Long roleId) {
        startPage();
        List<SysUser> list = userService.listUnallocated(user, roleId);
        return getDataTable(list);
    }

    @Operation(summary = "取消授权用户")
    @OperateLog(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    @PreAuthorize("hasAuthority('role:auth')")
    public Result<?> cancelAuthUser(@RequestBody SysUserRole sysUserRole) {
        // 校验角色权限
        sysRoleService.checkDataScopes(sysUserRole.getRoleId());
        // 校验用户权限
        userService.checkDataScopes(sysUserRole.getUserId());
        return toRes(sysUserRoleMapper.delete(sysUserRole.getUserId(), sysUserRole.getRoleId()));
    }

    @Operation(summary = "批量取消授权用户")
    @OperateLog(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    @PreAuthorize("hasAuthority('role:auth')")
    public Result<?> cancelAuthUserAll(@RequestParam Long roleId, @RequestParam Long[] userIds) {
        // 权限校验
        sysRoleService.checkDataScopes(roleId);
        // 校验用户权限
        userService.checkDataScopes(userIds);
        return toRes(sysUserRoleMapper.deleteBatch(roleId, userIds));
    }

    @Operation(summary = "批量选择用户授权")
    @OperateLog(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    @PreAuthorize("hasAuthority('role:auth')")
    public Result<?> selectAuthUserAll(@RequestParam Long roleId, @RequestParam Long[] userIds) {
        // 权限校验
        sysRoleService.checkDataScopes(roleId);
        sysRoleService.allocateUsers(roleId, userIds);
        return success();
    }

    @Operation(summary = "角色部门树列表")
    @GetMapping("/deptTree/{roleId}")
    @PreAuthorize("hasAuthority('role:query')")
    public Result<?> deptTree(@PathVariable Long roleId) {
        // 权限校验
        sysRoleService.checkDataScopes(roleId);
        Map<String, Object> res = new HashMap<>(2);
        res.put("checkedKeys", sysRoleDeptMapper.listDeptIdByRoleId(roleId));
        res.put("depts", sysDeptService.listDeptTree(new SysDept()));
        return success(res);
    }
}
