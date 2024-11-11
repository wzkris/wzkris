package com.wzkris.user.controller;

import com.wzkris.common.core.annotation.group.ValidationGroups;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.MapstructUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.excel.utils.ExcelUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.SysDept;
import com.wzkris.user.domain.SysPost;
import com.wzkris.user.domain.SysRole;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.dto.SysUserDTO;
import com.wzkris.user.domain.dto.SysUserRolesDTO;
import com.wzkris.user.domain.export.SysUserExport;
import com.wzkris.user.domain.vo.SysUserVO;
import com.wzkris.user.mapper.SysUserMapper;
import com.wzkris.user.mapper.SysUserPostMapper;
import com.wzkris.user.mapper.SysUserRoleMapper;
import com.wzkris.user.service.SysDeptService;
import com.wzkris.user.service.SysPostService;
import com.wzkris.user.service.SysRoleService;
import com.wzkris.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private final SysUserMapper userMapper;
    private final SysUserService userService;
    private final SysDeptService deptService;
    private final SysRoleService roleService;
    private final SysPostService postService;
    private final SysUserPostMapper userPostMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "用户分页列表")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('sys_user:list')")
    public Result<Page<SysUserVO>> listPage(SysUser user) {
        return ok(userService.listPage(user));
    }

    @Operation(summary = "用户详细信息")
    @GetMapping({"/{userId}", "/"})
    @PreAuthorize("@ps.hasPerms('sys_user:query')")
    public Result<?> getInfo(@PathVariable(required = false) Long userId) {
        // 校验权限
        userService.checkDataScopes(userId);
        SysUser user = userMapper.selectById(userId);

        Map<String, Object> info = new HashMap<>(8);
        // 用户信息
        info.put("user", user);
        // 可授权角色、岗位、部门
        info.put("roles", roleService.list(new SysRole(CommonConstants.STATUS_ENABLE)));
        info.put("posts", postService.list(new SysPost(CommonConstants.STATUS_ENABLE)));
        info.put("depts", deptService.listDeptTree(new SysDept(CommonConstants.STATUS_ENABLE)));
        // 已授权角色与岗位
        info.put("postIds", userPostMapper.listPostIdByUserId(userId));
        info.put("roleIds", userRoleMapper.listRoleIdByUserId(userId));

        return ok(info);
    }

    @Operation(summary = "新增用户")
    @OperateLog(title = "后台管理", subTitle = "新增用户", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('sys_user:add')")
    public Result<?> add(@Validated(ValidationGroups.Insert.class) @RequestBody SysUserDTO userDTO) {
        if (userService.checkUserUnique(new SysUser(userDTO.getUserId()).setUsername(userDTO.getUsername()))) {
            return fail("修改用户'" + userDTO.getUsername() + "'失败，登录账号已存在");
        }
        else if (StringUtil.isNotEmpty(userDTO.getPhoneNumber())
                && userService.checkUserUnique(new SysUser(userDTO.getUserId()).setPhoneNumber(userDTO.getPhoneNumber()))) {
            return fail("修改用户'" + userDTO.getUsername() + "'失败，手机号码已存在");
        }
        else if (StringUtil.isNotEmpty(userDTO.getEmail())
                && userService.checkUserUnique(new SysUser(userDTO.getUserId()).setEmail(userDTO.getEmail()))) {
            return fail("修改用户'" + userDTO.getUsername() + "'失败，邮箱账号已存在");
        }
        userService.insertUser(userDTO);
        return ok();
    }

    @Operation(summary = "修改用户")
    @OperateLog(title = "后台管理", subTitle = "修改用户", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('sys_user:edit')")
    public Result<?> edit(@Validated @RequestBody SysUserDTO userDTO) {
        // 校验权限
        userService.checkDataScopes(userDTO.getUserId());
        if (userService.checkUserUnique(new SysUser(userDTO.getUserId()).setUsername(userDTO.getUsername()))) {
            return fail("修改用户'" + userDTO.getUsername() + "'失败，登录账号已存在");
        }
        else if (StringUtil.isNotEmpty(userDTO.getPhoneNumber())
                && userService.checkUserUnique(new SysUser(userDTO.getUserId()).setPhoneNumber(userDTO.getPhoneNumber()))) {
            return fail("修改用户'" + userDTO.getUsername() + "'失败，手机号码已存在");
        }
        else if (StringUtil.isNotEmpty(userDTO.getEmail())
                && userService.checkUserUnique(new SysUser(userDTO.getUserId()).setEmail(userDTO.getEmail()))) {
            return fail("修改用户'" + userDTO.getUsername() + "'失败，邮箱账号已存在");
        }
        userService.updateUser(userDTO);
        return ok();
    }

    @Operation(summary = "删除用户")
    @OperateLog(title = "后台管理", subTitle = "删除用户", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('sys_user:remove')")
    public Result<?> remove(@RequestBody List<Long> userIds) {
        // 校验权限
        userService.checkDataScopes(userIds);
        return toRes(userMapper.deleteByIds(userIds));// soft delete
    }

    @Operation(summary = "重置密码")
    @OperateLog(title = "后台管理", subTitle = "重置密码", operateType = OperateType.UPDATE)
    @PostMapping("/reset_password")
    @PreAuthorize("@ps.hasPerms('sys_user:edit')")
    public Result<?> resetPwd(@RequestBody SysUser user) {
        // 校验权限
        userService.checkDataScopes(user.getUserId());

        SysUser update = new SysUser(user.getUserId());
        update.setPassword(passwordEncoder.encode(user.getPassword()));
        return toRes(userMapper.updateById(update));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "后台管理", subTitle = "状态修改", operateType = OperateType.UPDATE)
    @PostMapping("/edit_status")
    @PreAuthorize("@ps.hasPerms('sys_user:edit')")
    public Result<?> editStatus(@RequestBody SysUser user) {
        // 校验权限
        userService.checkDataScopes(user.getUserId());
        SysUser update = new SysUser(user.getUserId());
        update.setStatus(user.getStatus());
        return toRes(userMapper.updateById(update));
    }

    @Operation(summary = "导出")
    @OperateLog(title = "后台管理", subTitle = "导出用户数据", operateType = OperateType.EXPORT)
    @PostMapping("/export")
    @PreAuthorize("@ps.hasPerms('sys_user:export')")
    public void export(HttpServletResponse response, SysUser user) {
        List<SysUserVO> list = userService.list(user);
        List<SysUserExport> convert = MapstructUtil.convert(list, SysUserExport.class);
        ExcelUtil.exportExcel(convert, "后台用户数据", SysUserExport.class, response);
    }

    @Operation(summary = "根据用户id获取授权角色")
    @GetMapping("/authorize_role/{userId}")
    @PreAuthorize("@ps.hasPerms('sys_user:query')")
    public Result<?> authRole(@PathVariable Long userId) {
        // 校验权限
        userService.checkDataScopes(userId);
        SysUser sysUser = userMapper.selectById(userId);

        // 可授权角色必须根据租户来
        Map<String, Object> info = new HashMap<>(4);
        // 被授权用户信息
        info.put("user", sysUser);
        // 用户角色
        info.put("roleIds", userRoleMapper.listRoleIdByUserId(userId));
        // 可授权角色
        info.put("roles", roleService.list(new SysRole(CommonConstants.STATUS_ENABLE)));
        return ok(info);
    }

    @Operation(summary = "用户授权角色")
    @OperateLog(title = "后台管理", subTitle = "用户授权角色", operateType = OperateType.GRANT)
    @PostMapping("/authorize_role")
    @PreAuthorize("@ps.hasPerms('sys_user:edit')")
    public Result<?> authRole(@RequestBody @Valid SysUserRolesDTO userRolesDTO) {
        // 校验用户可操作权限
        userService.checkDataScopes(userRolesDTO.getUserId());
        // 校验角色可操作权限
        roleService.checkDataScopes(userRolesDTO.getRoleIds());
        // 分配权限
        userService.allocateRoles(userRolesDTO.getUserId(), userRolesDTO.getRoleIds());
        return ok();
    }
}
