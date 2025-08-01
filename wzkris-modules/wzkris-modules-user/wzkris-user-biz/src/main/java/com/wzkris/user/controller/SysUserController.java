package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.annotation.group.ValidationGroups;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.excel.utils.ExcelUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckSystemPerms;
import com.wzkris.common.security.annotation.enums.CheckMode;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.export.SysUserExport;
import com.wzkris.user.domain.req.*;
import com.wzkris.user.domain.vo.CheckedSelectVO;
import com.wzkris.user.domain.vo.SelectTreeVO;
import com.wzkris.user.domain.vo.SysUserVO;
import com.wzkris.user.listener.event.CreateUserEvent;
import com.wzkris.user.manager.SysDeptDataScopeManager;
import com.wzkris.user.manager.SysRoleDataScopeManager;
import com.wzkris.user.manager.SysUserDataScopeManager;
import com.wzkris.user.mapper.SysUserMapper;
import com.wzkris.user.service.SysRoleService;
import com.wzkris.user.service.SysTenantService;
import com.wzkris.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 系统用户管理
 *
 * @author wzkris
 */
@Tag(name = "系统用户")
@Validated
@RestController
@RequestMapping("/sys_user")
@RequiredArgsConstructor
public class SysUserController extends BaseController {

    private final SysUserMapper userMapper;

    private final SysUserService userService;

    private final SysUserDataScopeManager userDataScopeManager;

    private final SysDeptDataScopeManager deptDataScopeManager;

    private final SysRoleDataScopeManager roleDataScopeManager;

    private final SysRoleService roleService;

    private final SysTenantService tenantService;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "用户分页列表")
    @GetMapping("/list")
    @CheckSystemPerms("sys_user:list")
    public Result<Page<SysUserVO>> listPage(SysUserQueryReq queryReq) {
        startPage();
        List<SysUserVO> list = userDataScopeManager.listVO(this.buildPageWrapper(queryReq));
        return getDataTable(list);
    }

    private QueryWrapper<SysUser> buildPageWrapper(SysUserQueryReq queryReq) {
        return new QueryWrapper<SysUser>()
                .eq(ObjectUtils.isNotEmpty(queryReq.getTenantId()), "u.tenant_id", queryReq.getTenantId())
                .like(ObjectUtils.isNotEmpty(queryReq.getUsername()), "username", queryReq.getUsername())
                .like(ObjectUtils.isNotEmpty(queryReq.getNickname()), "nickname", queryReq.getNickname())
                .like(ObjectUtils.isNotEmpty(queryReq.getPhoneNumber()), "phone_number", queryReq.getPhoneNumber())
                .like(ObjectUtils.isNotEmpty(queryReq.getEmail()), "u.email", queryReq.getEmail())
                .eq(ObjectUtils.isNotEmpty(queryReq.getStatus()), "u.status", queryReq.getStatus())
                .eq(ObjectUtils.isNotEmpty(queryReq.getDeptId()), "u.dept_id", queryReq.getDeptId())
                .between(queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        "u.create_at",
                        queryReq.getParam("beginTime"),
                        queryReq.getParam("endTime"))
                .orderByDesc("u.user_id");
    }

    @Operation(summary = "用户-部门选择树")
    @GetMapping("/dept_select_tree")
    @CheckSystemPerms(
            value = {"sys_user:edit", "sys_user:add"},
            mode = CheckMode.OR)
    public Result<List<SelectTreeVO>> deptSelectTree(String deptName) {
        return ok(deptDataScopeManager.listSelectTree(deptName));
    }

    @Operation(summary = "用户-角色选择列表")
    @GetMapping({"/role_checked_select/", "/role_checked_select/{userId}"})
    @CheckSystemPerms(
            value = {"sys_user:edit", "sys_user:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectVO> roleSelect(@PathVariable(required = false) Long userId, String roleName) {
        userDataScopeManager.checkDataScopes(userId);
        CheckedSelectVO checkedSelectVO = new CheckedSelectVO();
        checkedSelectVO.setCheckedKeys(userId == null ? Collections.emptyList() : roleService.listIdByUserId(userId));
        checkedSelectVO.setSelects(roleDataScopeManager.listSelect(roleName));
        return ok(checkedSelectVO);
    }

    @Operation(summary = "用户详细信息")
    @GetMapping("/{userId}")
    @CheckSystemPerms("sys_user:query")
    public Result<SysUser> getInfo(@PathVariable Long userId) {
        // 校验权限
        userDataScopeManager.checkDataScopes(userId);
        return ok(userMapper.selectById(userId));
    }

    @Operation(summary = "新增用户")
    @OperateLog(title = "系统用户", subTitle = "新增用户", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckSystemPerms("sys_user:add")
    public Result<Void> add(@Validated(ValidationGroups.Insert.class) @RequestBody SysUserReq userReq) {
        if (!tenantService.checkAccountLimit(SystemUserUtil.getTenantId())) {
            return err412("账号数量已达上限，请联系管理员");
        } else if (userService.checkExistByUsername(userReq.getUserId(), userReq.getUsername())) {
            return err412("修改用户'" + userReq.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(userReq.getPhoneNumber())
                && userService.checkExistByPhoneNumber(userReq.getUserId(), userReq.getPhoneNumber())) {
            return err412("修改用户'" + userReq.getUsername() + "'失败，手机号码已存在");
        }
        SysUser user = BeanUtil.convert(userReq, SysUser.class);
        String password = RandomStringUtils.secure().next(8);
        user.setPassword(password);

        boolean success = userService.insertUser(user, userReq.getRoleIds());
        if (success) {
            SpringUtil.getContext()
                    .publishEvent(new CreateUserEvent(SystemUserUtil.getUserId(), userReq.getUsername(), password));
        }
        return toRes(success);
    }

    @Operation(summary = "修改用户")
    @OperateLog(title = "系统用户", subTitle = "修改用户", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckSystemPerms("sys_user:edit")
    public Result<Void> edit(@Validated @RequestBody SysUserReq userReq) {
        // 校验权限
        userDataScopeManager.checkDataScopes(userReq.getUserId());
        if (userService.checkExistByUsername(userReq.getUserId(), userReq.getUsername())) {
            return err412("修改用户'" + userReq.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(userReq.getPhoneNumber())
                && userService.checkExistByPhoneNumber(userReq.getUserId(), userReq.getPhoneNumber())) {
            return err412("修改用户'" + userReq.getUsername() + "'失败，手机号码已存在");
        }
        SysUser user = BeanUtil.convert(userReq, SysUser.class);

        return toRes(userService.updateUser(user, userReq.getRoleIds()));
    }

    @Operation(summary = "修改用户授权角色")
    @OperateLog(title = "系统用户", subTitle = "授权用户角色", operateType = OperateType.GRANT)
    @PostMapping("/edit/grant_role")
    @CheckSystemPerms("sys_user:grant_role")
    public Result<Void> editGrantRole(@RequestBody @Valid SysUser2RolesReq req) {
        // 校验用户可操作权限
        userDataScopeManager.checkDataScopes(req.getUserId());
        // 校验角色可操作权限
        roleDataScopeManager.checkDataScopes(req.getRoleIds());
        return toRes(userService.updateGrantRoles(req.getUserId(), req.getRoleIds()));
    }

    @Operation(summary = "删除用户")
    @OperateLog(title = "系统用户", subTitle = "删除用户", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckSystemPerms("sys_user:remove")
    public Result<Void> remove(@RequestBody List<Long> userIds) {
        // 校验权限
        userDataScopeManager.checkDataScopes(userIds);
        if (tenantService.checkAdministrator(userIds)) {
            return err412("删除失败，用户包含租户超级管理员");
        }
        return toRes(userService.deleteByIds(userIds));
    }

    @Operation(summary = "重置密码")
    @OperateLog(title = "系统用户", subTitle = "重置密码", operateType = OperateType.UPDATE)
    @PostMapping("/reset_password")
    @CheckSystemPerms("sys_user:edit")
    public Result<Void> resetPwd(@RequestBody @Valid ResetPwdReq req) {
        // 校验权限
        userDataScopeManager.checkDataScopes(req.getId());

        SysUser update = new SysUser(req.getId());
        update.setPassword(passwordEncoder.encode(req.getPassword()));
        return toRes(userMapper.updateById(update));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "系统用户", subTitle = "状态修改", operateType = OperateType.UPDATE)
    @PostMapping("/edit_status")
    @CheckSystemPerms("sys_user:edit")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        // 校验权限
        userDataScopeManager.checkDataScopes(statusReq.getId());
        SysUser update = new SysUser(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(userMapper.updateById(update));
    }

    @Operation(summary = "导出")
    @OperateLog(title = "系统用户", subTitle = "导出用户数据", operateType = OperateType.EXPORT)
    @GetMapping("/export")
    @CheckSystemPerms("sys_user:export")
    public void export(HttpServletResponse response, SysUserQueryReq queryReq) {
        List<SysUserVO> list = userDataScopeManager.listVO(this.buildPageWrapper(queryReq));
        List<SysUserExport> convert = BeanUtil.convert(list, SysUserExport.class);
        ExcelUtil.exportExcel(convert, "后台用户数据", SysUserExport.class, response);
    }

}
