package com.thingslink.user.controller;

import com.thingslink.common.core.annotation.group.ValidationGroups;
import com.thingslink.common.core.constant.CommonConstants;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.OperateType;
import com.thingslink.common.orm.model.BaseController;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.orm.utils.DynamicTenantUtil;
import com.thingslink.user.domain.SysDept;
import com.thingslink.user.domain.SysPost;
import com.thingslink.user.domain.SysRole;
import com.thingslink.user.domain.SysUser;
import com.thingslink.user.domain.dto.SysUserDTO;
import com.thingslink.user.domain.dto.SysUserRolesDTO;
import com.thingslink.user.domain.vo.SysUserVO;
import com.thingslink.user.mapper.SysUserMapper;
import com.thingslink.user.mapper.SysUserPostMapper;
import com.thingslink.user.mapper.SysUserRoleMapper;
import com.thingslink.user.service.SysDeptService;
import com.thingslink.user.service.SysPostService;
import com.thingslink.user.service.SysRoleService;
import com.thingslink.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理
 *
 * @author wzkris
 */
@Tag(name = "用户管理")
@Validated
@RestController
@RequestMapping("/sys_user")
@RequiredArgsConstructor
public class SysUserController extends BaseController {
    private final SysUserMapper sysUserMapper;
    private final SysUserService sysUserService;
    private final SysDeptService sysDeptService;
    private final SysRoleService sysRoleService;
    private final SysPostService sysPostService;
    private final SysUserPostMapper sysUserPostMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final PasswordEncoder passwordEncoder;


    @Operation(summary = "用户分页列表")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('user:list')")
    public Result<Page<SysUserVO>> listPage(SysUser user) {
        return success(sysUserService.listPage(user));
    }

    @Operation(summary = "用户详细信息")
    @GetMapping({"/{userId}", "/"})
    @PreAuthorize("@ps.hasPerms('user:query')")
    public Result<?> getInfo(@PathVariable(required = false) Long userId) {
        // 校验权限
        sysUserService.checkDataScopes(Collections.singletonList(userId));
        SysUser user = sysUserMapper.selectById(userId);
        Map<String, Object> res = new HashMap<>(8);
        // 用户信息
        res.put("user", user);
        // 可授权角色、岗位、部门
        res.put("roles", sysRoleService.list(new SysRole(CommonConstants.STATUS_ENABLE)));
        res.put("posts", sysPostService.list(new SysPost(CommonConstants.STATUS_ENABLE)));
        res.put("depts", sysDeptService.listDeptTree(new SysDept(CommonConstants.STATUS_ENABLE)));
        // 已授权角色与岗位
        res.put("postIds", sysUserPostMapper.listPostIdByUserId(userId));
        res.put("roleIds", sysUserRoleMapper.listRoleIdByUserId(userId));
        return success(res);
    }

    @Operation(summary = "新增用户")
    @OperateLog(title = "后台管理", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('user:add')")
    public Result<?> add(@Validated(ValidationGroups.Insert.class) @RequestBody SysUserDTO userDTO) {
        // 校验租户ID
        sysUserService.checkTenantId(userDTO);
        if (sysUserService.checkUserUnique(new SysUser().setUsername(userDTO.getUsername()), userDTO.getUserId())) {
            return fail("修改用户'" + userDTO.getUsername() + "'失败，登录账号已存在");
        }
        else if (StringUtil.isNotEmpty(userDTO.getPhoneNumber())
                && sysUserService.checkUserUnique(new SysUser().setPhoneNumber(userDTO.getPhoneNumber()), userDTO.getUserId())) {
            return fail("修改用户'" + userDTO.getUsername() + "'失败，手机号码已存在");
        }
        else if (StringUtil.isNotEmpty(userDTO.getEmail())
                && sysUserService.checkUserUnique(new SysUser().setEmail(userDTO.getEmail()), userDTO.getUserId())) {
            return fail("修改用户'" + userDTO.getUsername() + "'失败，邮箱账号已存在");
        }
        return toRes(sysUserService.insertUser(userDTO));
    }

    @Operation(summary = "修改用户")
    @OperateLog(title = "后台管理", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('user:edit')")
    public Result<?> edit(@Validated @RequestBody SysUserDTO userDTO) {
        // 校验权限
        sysUserService.checkDataScopes(Collections.singletonList(userDTO.getUserId()));
        // 校验租户ID
        sysUserService.checkTenantId(userDTO);
        if (sysUserService.checkUserUnique(new SysUser().setUsername(userDTO.getUsername()), userDTO.getUserId())) {
            return fail("修改用户'" + userDTO.getUsername() + "'失败，登录账号已存在");
        }
        else if (StringUtil.isNotEmpty(userDTO.getPhoneNumber())
                && sysUserService.checkUserUnique(new SysUser().setPhoneNumber(userDTO.getPhoneNumber()), userDTO.getUserId())) {
            return fail("修改用户'" + userDTO.getUsername() + "'失败，手机号码已存在");
        }
        else if (StringUtil.isNotEmpty(userDTO.getEmail())
                && sysUserService.checkUserUnique(new SysUser().setEmail(userDTO.getEmail()), userDTO.getUserId())) {
            return fail("修改用户'" + userDTO.getUsername() + "'失败，邮箱账号已存在");
        }
        return toRes(sysUserService.updateUser(userDTO));
    }

    @Operation(summary = "删除用户")
    @OperateLog(title = "后台管理", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('user:remove')")
    public Result<?> remove(@RequestBody List<Long> userIds) {
        // 校验权限
        sysUserService.checkDataScopes(userIds);
        return toRes(sysUserMapper.deleteByIds(userIds));
    }

    @Operation(summary = "重置密码")
    @OperateLog(title = "后台管理", operateType = OperateType.UPDATE)
    @PostMapping("/reset_password")
    @PreAuthorize("@ps.hasPerms('user:edit')")
    public Result<?> resetPwd(@RequestBody SysUser user) {
        // 校验权限
        sysUserService.checkDataScopes(Collections.singletonList(user.getUserId()));

        SysUser update = new SysUser(user.getUserId());
        update.setPassword(passwordEncoder.encode(user.getPassword()));
        return toRes(sysUserMapper.updateById(update));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "后台管理", operateType = OperateType.UPDATE)
    @PostMapping("/edit_status")
    @PreAuthorize("@ps.hasPerms('user:edit')")
    public Result<?> editStatus(@RequestBody SysUser user) {
        // 校验权限
        sysUserService.checkDataScopes(Collections.singletonList(user.getUserId()));
        SysUser update = new SysUser(user.getUserId());
        update.setStatus(user.getStatus());
        return toRes(sysUserMapper.updateById(update));
    }

    @Operation(summary = "根据用户id获取授权角色")
    @GetMapping("/authorize_role/{userId}")
    @PreAuthorize("@ps.hasPerms('user:query')")
    public Result<?> authRole(@PathVariable Long userId) {
        // 校验权限
        sysUserService.checkDataScopes(Collections.singletonList(userId));
        SysUser sysUser = sysUserMapper.selectById(userId);
        // 可授权角色必须根据租户来
        DynamicTenantUtil.set(sysUser.getTenantId());
        Map<String, Object> res = new HashMap<>(4);
        // 被授权用户信息
        res.put("user", sysUser);
        // 用户角色
        res.put("roleIds", sysUserRoleMapper.listRoleIdByUserId(userId));
        // 可授权角色
        res.put("roles", sysRoleService.list(new SysRole(CommonConstants.STATUS_ENABLE)));
        return success(res);
    }

    @Operation(summary = "用户授权角色")
    @OperateLog(title = "后台管理", operateType = OperateType.GRANT)
    @PostMapping("/authorize_role")
    @PreAuthorize("@ps.hasPerms('user:edit')")
    public Result<?> authRole(@RequestBody @Valid SysUserRolesDTO userRolesDTO) {
        // 校验用户可操作权限
        sysUserService.checkDataScopes(Collections.singletonList(userRolesDTO.getUserId()));
        // 校验角色可操作权限
        sysRoleService.checkDataScopes(userRolesDTO.getRoleIds());
        // 分配权限
        sysUserService.allocateRoles(userRolesDTO.getUserId(), userRolesDTO.getRoleIds());
        return success();
    }
}
