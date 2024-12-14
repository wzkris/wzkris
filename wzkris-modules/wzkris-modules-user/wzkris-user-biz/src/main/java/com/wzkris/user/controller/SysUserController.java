package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.annotation.group.ValidationGroups;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.MapstructUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.excel.utils.ExcelUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.security.utils.SysUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.dto.SysUserDTO;
import com.wzkris.user.domain.export.SysUserExport;
import com.wzkris.user.domain.req.EditStatusReq;
import com.wzkris.user.domain.req.ResetPwdReq;
import com.wzkris.user.domain.req.SysUser2RolesReq;
import com.wzkris.user.domain.req.SysUserQueryReq;
import com.wzkris.user.domain.vo.SelectTreeVO;
import com.wzkris.user.domain.vo.SysUserGrantVO;
import com.wzkris.user.domain.vo.SysUserVO;
import com.wzkris.user.mapper.SysUserMapper;
import com.wzkris.user.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final SysTenantService tenantService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "用户分页列表")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('sys_user:list')")
    public Result<Page<SysUserVO>> listPage(SysUserQueryReq queryReq) {
        startPage();
        List<SysUserVO> list = userMapper.selectVOInScope(this.buildPageWrapper(queryReq));
        return getDataTable(list);
    }

    private QueryWrapper<SysUser> buildPageWrapper(SysUserQueryReq queryReq) {
        return new QueryWrapper<SysUser>()
                .eq(StringUtil.isNotNull(queryReq.getTenantId()), "u.tenant_id", queryReq.getTenantId())
                .like(StringUtil.isNotNull(queryReq.getUsername()), "username", queryReq.getUsername())
                .like(StringUtil.isNotNull(queryReq.getNickname()), "nickname", queryReq.getNickname())
                .like(StringUtil.isNotNull(queryReq.getPhoneNumber()), "phone_number", queryReq.getPhoneNumber())
                .like(StringUtil.isNotNull(queryReq.getEmail()), "email", queryReq.getEmail())
                .eq(StringUtil.isNotNull(queryReq.getStatus()), "u.status", queryReq.getStatus())
                .eq(StringUtil.isNotNull(queryReq.getDeptId()), "u.dept_id", queryReq.getDeptId())
                .between(queryReq.getParams().get("beginTime") != null && queryReq.getParams().get("endTime") != null,
                        "u.create_id", queryReq.getParams().get("beginTime"), queryReq.getParams().get("endTime"))
                .orderByDesc("u.user_id");
    }

    @Operation(summary = "部门选择树")
    @GetMapping("/dept_tree")
    @PreAuthorize("@ps.hasPerms('sys_user:list')")
    public Result<List<SelectTreeVO>> deptTree(String deptName) {
        return ok(deptService.listSelectTree(deptName));
    }

    @Operation(summary = "用户详细信息")
    @GetMapping({"/{userId}", "/"})
    @PreAuthorize("@ps.hasPerms('sys_user:query')")
    public Result<SysUserGrantVO> getInfo(@PathVariable(required = false) Long userId) {
        // 校验权限
        userService.checkDataScopes(userId);

        SysUserGrantVO resp = new SysUserGrantVO();
        // 可授权角色、岗位、部门
        resp.setRoles(roleService.listCanGranted());
        resp.setPosts(postService.listCanGranted());
        if (StringUtil.isNotNull(userId)) {
            resp.setUser(userMapper.selectById(userId));
            // 已授权角色与岗位
            resp.setRoleIds(roleService.listIdByUserId(userId));
            resp.setPostIds(postService.listIdByUserId(userId));
        }
        return ok(resp);
    }

    @Operation(summary = "新增用户")
    @OperateLog(title = "后台管理", subTitle = "新增用户", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('sys_user:add')")
    public Result<Void> add(@Validated(ValidationGroups.Insert.class) @RequestBody SysUserDTO userDTO) {
        if (!tenantService.checkAccountLimit(SysUtil.getTenantId())) {
            return fail("账号数量已达上限，请联系管理员");
        }
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
        // 密码加密
        if (StringUtil.isNotBlank(userDTO.getPassword())) {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        userService.insertUser(userDTO);
        return ok();
    }

    @Operation(summary = "修改用户")
    @OperateLog(title = "后台管理", subTitle = "修改用户", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('sys_user:edit')")
    public Result<Void> edit(@Validated @RequestBody SysUserDTO userDTO) {
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
    public Result<Void> remove(@RequestBody List<Long> userIds) {
        // 校验权限
        userService.checkDataScopes(userIds);
        if (tenantService.checkAdministrator(userIds)) {
            return fail("删除失败，用户包含租户超级管理员");
        }
        userService.deleteByIds(userIds);
        return ok();
    }

    @Operation(summary = "重置密码")
    @OperateLog(title = "后台管理", subTitle = "重置密码", operateType = OperateType.UPDATE)
    @PostMapping("/reset_password")
    @PreAuthorize("@ps.hasPerms('sys_user:edit')")
    public Result<Void> resetPwd(@RequestBody @Valid ResetPwdReq req) {
        // 校验权限
        userService.checkDataScopes(req.getUserId());

        SysUser update = new SysUser(req.getUserId());
        update.setPassword(passwordEncoder.encode(req.getPassword()));
        return toRes(userMapper.updateById(update));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "后台管理", subTitle = "状态修改", operateType = OperateType.UPDATE)
    @PostMapping("/edit_status")
    @PreAuthorize("@ps.hasPerms('sys_user:edit')")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        // 校验权限
        userService.checkDataScopes(statusReq.getId());
        SysUser update = new SysUser(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(userMapper.updateById(update));
    }

    @Operation(summary = "导出")
    @OperateLog(title = "后台管理", subTitle = "导出用户数据", operateType = OperateType.EXPORT)
    @PostMapping("/export")
    @PreAuthorize("@ps.hasPerms('sys_user:export')")
    public void export(HttpServletResponse response, SysUserQueryReq queryReq) {
        List<SysUserVO> list = userMapper.selectVOInScope(this.buildPageWrapper(queryReq));
        List<SysUserExport> convert = MapstructUtil.convert(list, SysUserExport.class);
        ExcelUtil.exportExcel(convert, "后台用户数据", SysUserExport.class, response);
    }

    @Operation(summary = "根据用户id获取授权角色")
    @GetMapping("/authorize_role/{userId}")
    @PreAuthorize("@ps.hasPerms('sys_user:query')")
    public Result<SysUserGrantVO> authRole(@PathVariable Long userId) {
        // 校验权限
        userService.checkDataScopes(userId);

        SysUserGrantVO resp = new SysUserGrantVO();
        resp.setUser(userMapper.selectById(userId));
        resp.setRoleIds(roleService.listIdByUserId(userId));
        resp.setRoles(roleService.listCanGranted());
        return ok(resp);
    }

    @Operation(summary = "用户授权角色")
    @OperateLog(title = "后台管理", subTitle = "授权用户角色", operateType = OperateType.GRANT)
    @PostMapping("/authorize_role")
    @PreAuthorize("@ps.hasPerms('sys_user:edit')")
    public Result<Void> authRole(@RequestBody @Valid SysUser2RolesReq req) {
        // 校验用户可操作权限
        userService.checkDataScopes(req.getUserId());
        // 校验角色可操作权限
        roleService.checkDataScopes(req.getRoleIds());
        // 分配权限
        userService.allocateRoles(req.getUserId(), req.getRoleIds());
        return ok();
    }

}
