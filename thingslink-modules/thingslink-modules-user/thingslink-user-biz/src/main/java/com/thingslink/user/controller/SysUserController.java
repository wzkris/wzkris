package com.thingslink.user.controller;

import com.thingslink.user.domain.SysDept;
import com.thingslink.user.domain.SysPost;
import com.thingslink.user.domain.SysRole;
import com.thingslink.user.domain.SysUser;
import com.thingslink.user.domain.dto.SysUserDTO;
import com.thingslink.user.domain.vo.SysUserVO;
import com.thingslink.user.mapper.SysUserMapper;
import com.thingslink.user.mapper.SysUserPostMapper;
import com.thingslink.user.mapper.SysUserRoleMapper;
import com.thingslink.user.service.SysDeptService;
import com.thingslink.user.service.SysPostService;
import com.thingslink.user.service.SysRoleService;
import com.thingslink.user.service.SysUserService;
import com.thingslink.common.core.constant.CommonConstants;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.core.annotation.group.ValidationGroups;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.OperateType;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.security.utils.LoginUserUtil;
import com.thingslink.common.orm.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理
 *
 * @author wzkris
 */
@Tag(name = "用户管理")
@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SysUserController extends BaseController {
    private final SysUserMapper sysUserMapper;
    private final SysUserService sysUserService;
    private final SysDeptService sysDeptService;
    private final SysRoleService sysRoleService;
    private final SysPostService sysPostService;
    private final SysUserPostMapper sysUserPostMapper;
    private final SysUserRoleMapper sysUserRoleMapper;

    @Operation(summary = "用户分页列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('user:list')")
    public Result<Page<SysUserVO>> listPage(SysUser user) {
        return success(sysUserService.listPage(user));
    }

    @Operation(summary = "用户详细信息")
    @GetMapping({"/{userId}", "/"})
    @PreAuthorize("hasAuthority('user:query')")
    public Result<?> getInfo(@PathVariable(required = false) Long userId) {
        // 校验权限
        sysUserService.checkDataScopes(userId);
        SysUser user = sysUserMapper.selectById(userId);
        // 未指定用户则默认为自身租户
        Long specifyTenantId = user == null ? LoginUserUtil.getTenantId() : user.getTenantId();
        Map<String, Object> res = new HashMap<>(8);
        // 用户信息
        res.put("user", user);
        // 可授权角色、岗位、部门
        res.put("roles", sysRoleService.list(new SysRole(CommonConstants.STATUS_ENABLE).setTenantId(specifyTenantId)));
        res.put("posts", sysPostService.list(new SysPost(CommonConstants.STATUS_ENABLE).setTenantId(specifyTenantId)));
        res.put("depts", sysDeptService.listDeptTree(new SysDept(CommonConstants.STATUS_ENABLE).setTenantId(specifyTenantId)));
        // 已授权角色与岗位
        res.put("postIds", sysUserPostMapper.listPostIdByUserId(userId));
        res.put("roleIds", sysUserRoleMapper.listRoleIdByUserId(userId));
        return success(res);
    }

    @Operation(summary = "新增用户")
    @OperateLog(title = "后台管理", operateType = OperateType.INSERT)
    @PostMapping
    @PreAuthorize("hasAuthority('user:add')")
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
    @PutMapping
    @PreAuthorize("hasAuthority('user:edit')")
    public Result<?> edit(@Validated @RequestBody SysUserDTO userDTO) {
        // 校验权限
        sysUserService.checkDataScopes(userDTO.getUserId());
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
    @DeleteMapping("/{userIds}")
    @PreAuthorize("hasAuthority('user:remove')")
    public Result<?> remove(@PathVariable Long[] userIds) {
        // 校验权限
        sysUserService.checkDataScopes(userIds);
        return toRes(sysUserMapper.deleteBatchIds(Arrays.asList(userIds)));
    }

    @Operation(summary = "重置密码")
    @OperateLog(title = "后台管理", operateType = OperateType.UPDATE)
    @PutMapping("/resetPwd")
    @PreAuthorize("hasAuthority('user:edit')")
    public Result<?> resetPwd(@RequestBody SysUser user) {
        // 校验权限
        sysUserService.checkDataScopes(user.getUserId());
        SysUser update = new SysUser(user.getUserId());
        update.setPassword(LoginUserUtil.encryptPassword(user.getPassword()));
        return toRes(sysUserMapper.updateById(update));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "后台管理", operateType = OperateType.UPDATE)
    @PutMapping("/changeStatus")
    @PreAuthorize("hasAuthority('user:edit')")
    public Result<?> changeStatus(@RequestBody SysUser user) {
        // 校验权限
        sysUserService.checkDataScopes(user.getUserId());
        SysUser update = new SysUser(user.getUserId());
        update.setStatus(user.getStatus());
        return toRes(sysUserMapper.updateById(update));
    }

    @Operation(summary = "根据用户id获取授权角色")
    @GetMapping("/authRole/{userId}")
    @PreAuthorize("hasAuthority('user:query')")
    public Result<?> authRole(@PathVariable Long userId) {
        // 校验权限
        sysUserService.checkDataScopes(userId);
        Map<String, Object> res = new HashMap<>(4);
        // 被授权用户信息
        res.put("user", sysUserMapper.selectById(userId));
        // 用户角色
        res.put("roleIds", sysUserRoleMapper.listRoleIdByUserId(userId));
        // 可授权角色
        res.put("roles", sysRoleService.list(new SysRole(CommonConstants.STATUS_ENABLE)));
        return success(res);
    }

    @Operation(summary = "用户授权角色")
    @OperateLog(title = "后台管理", operateType = OperateType.GRANT)
    @PutMapping("/authRole")
    @PreAuthorize("hasAuthority('user:edit')")
    public Result<?> authRole(Long userId, @NotEmpty(message = "[roleIds] {validate.notnull}") Long[] roleIds) {
        // 校验用户可操作权限
        sysUserService.checkDataScopes(userId);
        // 校验角色可操作权限
        sysRoleService.checkDataScopes(roleIds);
        // 分配权限
        sysUserService.allocateRoles(userId, roleIds);
        return success();
    }
}
