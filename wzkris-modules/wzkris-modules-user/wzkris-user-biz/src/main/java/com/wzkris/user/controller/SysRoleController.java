package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.annotation.group.ValidationGroups;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.oauth2.annotation.CheckPerms;
import com.wzkris.common.security.oauth2.annotation.CheckSystemPerms;
import com.wzkris.common.security.utils.LoginUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.SysRole;
import com.wzkris.user.domain.req.*;
import com.wzkris.user.domain.vo.CheckedSelectTreeVO;
import com.wzkris.user.domain.vo.SelectVO;
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
    @CheckSystemPerms("sys_role:list")
    public Result<Page<SysRole>> listPage(SysRoleQueryReq req) {
        startPage();
        List<SysRole> list = roleMapper.selectLists(this.buildQueryWrapper(req));
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
    @CheckSystemPerms("sys_role:query")
    public Result<SysRole> getInfo(@PathVariable Long roleId) {
        // 权限校验
        roleService.checkDataScopes(roleId);
        return ok(roleMapper.selectById(roleId));
    }

    @Operation(summary = "角色菜单选择树")
    @GetMapping({"/menu_checked_select_tree/", "/menu_checked_select_tree/{roleId}"})
    @CheckSystemPerms(value = {"sys_role:edit", "sys_role:add"}, mode = CheckPerms.Mode.OR)
    public Result<CheckedSelectTreeVO> roleMenuSelectTree(@PathVariable(required = false) Long roleId) {
        // 权限校验
        roleService.checkDataScopes(roleId);
        CheckedSelectTreeVO checkedSelectTreeVO = new CheckedSelectTreeVO();
        checkedSelectTreeVO.setCheckedKeys(roleId == null ? Collections.emptyList() : roleMenuMapper.listMenuIdByRoleIds(Collections.singletonList(roleId)));
        checkedSelectTreeVO.setSelectTrees(menuService.listSelectTree(LoginUtil.getUserId()));
        return ok(checkedSelectTreeVO);
    }

    @Operation(summary = "角色部门选择树")
    @GetMapping({"/dept_checked_select_tree/", "/dept_checked_select_tree/{roleId}"})
    @CheckSystemPerms(value = {"sys_role:edit", "sys_role:add"}, mode = CheckPerms.Mode.OR)
    public Result<CheckedSelectTreeVO> roleDeptSelectTree(@PathVariable(required = false) Long roleId) {
        // 权限校验
        roleService.checkDataScopes(roleId);
        CheckedSelectTreeVO checkedSelectTreeVO = new CheckedSelectTreeVO();
        checkedSelectTreeVO.setCheckedKeys(roleId == null ? Collections.emptyList() : roleDeptMapper.listDeptIdByRoleId(roleId));
        checkedSelectTreeVO.setSelectTrees(deptService.listSelectTree(null));
        return ok(checkedSelectTreeVO);
    }

    @Operation(summary = "新增角色")
    @OperateLog(title = "角色管理", subTitle = "新增角色", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckSystemPerms("sys_role:add")
    public Result<Void> add(@Validated @RequestBody SysRoleReq roleReq) {
        if (!tenantService.checkRoleLimit(LoginUtil.getTenantId())) {
            return err412("角色数量已达上限，请联系管理员");
        }
        return toRes(roleService.insertRole(BeanUtil.convert(roleReq, SysRole.class), roleReq.getMenuIds(), roleReq.getDeptIds()));
    }

    @Operation(summary = "修改角色")
    @OperateLog(title = "角色管理", subTitle = "修改角色", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckSystemPerms("sys_role:edit")
    public Result<Void> edit(@Validated(value = ValidationGroups.Update.class) @RequestBody SysRoleReq roleReq) {
        // 权限校验
        roleService.checkDataScopes(roleReq.getRoleId());
        return toRes(roleService.updateRole(BeanUtil.convert(roleReq, SysRole.class), roleReq.getMenuIds(), roleReq.getDeptIds()));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "系统用户", subTitle = "状态修改", operateType = OperateType.UPDATE)
    @PostMapping("/edit_status")
    @CheckSystemPerms("sys_role:edit")
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
    @CheckSystemPerms("sys_role:remove")
    public Result<Void> remove(@RequestBody @NotEmpty(message = "{desc.role}{desc.id}{validate.notnull}") List<Long> roleIds) {
        // 权限校验
        roleService.checkDataScopes(roleIds);
        roleService.checkRoleUsed(roleIds);
        return toRes(roleService.deleteByIds(roleIds));
    }

    @Operation(summary = "查询已授权的用户列表")
    @GetMapping("/authorize/allocated_list")
    @CheckSystemPerms("sys_role:grant_user")
    public Result<Page<SelectVO>> allocatedList(SysUserQueryReq queryReq, Long roleId) {
        // 校验角色权限
        roleService.checkDataScopes(roleId);
        startPage();
        List<SelectVO> list = userService.listAllocated(queryReq, roleId);
        return getDataTable(list);
    }

    @Operation(summary = "查询未授权的用户列表")
    @GetMapping("/authorize/unallocated_list")
    @CheckSystemPerms("sys_role:grant_user")
    public Result<Page<SelectVO>> unallocatedList(SysUserQueryReq queryReq, Long roleId) {
        // 校验角色权限
        roleService.checkDataScopes(roleId);
        startPage();
        List<SelectVO> list = userService.listUnallocated(queryReq, roleId);
        return getDataTable(list);
    }

    @Operation(summary = "取消授权")
    @OperateLog(title = "角色管理", subTitle = "取消授权", operateType = OperateType.GRANT)
    @PostMapping("/authorize_cancel")
    @CheckSystemPerms("sys_role:grant_user")
    public Result<Void> cancelAuth(@RequestBody @Valid SysRole2UsersReq req) {
        // 权限校验
        roleService.checkDataScopes(req.getRoleId());
        // 校验用户权限
        userService.checkDataScopes(req.getUserIds());
        return toRes(userRoleMapper.deleteBatch(req.getRoleId(), req.getUserIds()));
    }

    @Operation(summary = "角色授权")
    @OperateLog(title = "角色管理", subTitle = "授权用户", operateType = OperateType.GRANT)
    @PostMapping("/authorize_user")
    @CheckSystemPerms("sys_role:grant_user")
    public Result<Void> batchAuth(@RequestBody @Valid SysRole2UsersReq req) {
        // 权限校验
        roleService.checkDataScopes(req.getRoleId());
        // 校验用户权限
        userService.checkDataScopes(req.getUserIds());
        return toRes(roleService.allocateUsers(req.getRoleId(), req.getUserIds()));
    }

}
