package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.excel.utils.ExcelUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckSystemPerms;
import com.wzkris.common.security.annotation.enums.CheckMode;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.common.validator.group.ValidationGroups;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.user.domain.UserInfoDO;
import com.wzkris.user.domain.export.UserInfoExport;
import com.wzkris.user.domain.req.*;
import com.wzkris.user.domain.vo.CheckedSelectVO;
import com.wzkris.user.domain.vo.SelectTreeVO;
import com.wzkris.user.domain.vo.UserInfoManageVO;
import com.wzkris.user.listener.event.CreateUserEvent;
import com.wzkris.user.manager.DeptInfoDataScopeManager;
import com.wzkris.user.manager.RoleInfoDataScopeManager;
import com.wzkris.user.manager.UserInfoDataScopeManager;
import com.wzkris.user.mapper.UserInfoMapper;
import com.wzkris.user.service.RoleInfoService;
import com.wzkris.user.service.TenantInfoService;
import com.wzkris.user.service.UserInfoService;
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
@Tag(name = "用户管理")
@Validated
@RestController
@RequestMapping("/user-manage")
@RequiredArgsConstructor
public class UserManageController extends BaseController {

    private final UserInfoMapper userInfoMapper;

    private final UserInfoService userInfoService;

    private final RoleInfoService roleInfoService;

    private final TenantInfoService tenantInfoService;

    private final PasswordEncoder passwordEncoder;

    private final UserInfoDataScopeManager userInfoDataScopeManager;

    private final DeptInfoDataScopeManager deptInfoDataScopeManager;

    private final RoleInfoDataScopeManager roleInfoDataScopeManager;

    @Operation(summary = "用户分页列表")
    @GetMapping("/list")
    @CheckSystemPerms("user-mod:user-mng:list")
    public Result<Page<UserInfoManageVO>> listPage(UserManageQueryReq queryReq) {
        startPage();
        List<UserInfoManageVO> list = userInfoDataScopeManager.listVO(this.buildPageWrapper(queryReq));
        return getDataTable(list);
    }

    private QueryWrapper<UserInfoDO> buildPageWrapper(UserManageQueryReq queryReq) {
        return new QueryWrapper<UserInfoDO>()
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
    @GetMapping("/dept-selecttree")
    @CheckSystemPerms(
            value = {"user-mod:user-mng:edit", "user-mod:user-mng:add"},
            mode = CheckMode.OR)
    public Result<List<SelectTreeVO>> deptSelectTree(String deptName) {
        return ok(deptInfoDataScopeManager.listSelectTree(deptName));
    }

    @Operation(summary = "用户-角色选择列表")
    @GetMapping({"/role-checked-select/", "/role-checked-select/{userId}"})
    @CheckSystemPerms(
            value = {"user-mod:user-mng:edit", "user-mod:user-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectVO> roleSelect(@PathVariable(required = false) Long userId, String roleName) {
        userInfoDataScopeManager.checkDataScopes(userId);
        CheckedSelectVO checkedSelectVO = new CheckedSelectVO();
        checkedSelectVO.setCheckedKeys(userId == null ? Collections.emptyList() : roleInfoService.listIdByUserId(userId));
        checkedSelectVO.setSelects(roleInfoDataScopeManager.listSelect(roleName));
        return ok(checkedSelectVO);
    }

    @Operation(summary = "用户详细信息")
    @GetMapping("/{userId}")
    @CheckSystemPerms("user-mod:user-mng:query")
    public Result<UserInfoDO> getInfo(@PathVariable Long userId) {
        // 校验权限
        userInfoDataScopeManager.checkDataScopes(userId);
        return ok(userInfoMapper.selectById(userId));
    }

    @Operation(summary = "新增用户")
    @OperateLog(title = "系统用户", subTitle = "新增用户", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckSystemPerms("user-mod:user-mng:add")
    public Result<Void> add(@Validated(ValidationGroups.Insert.class) @RequestBody UserManageReq userReq) {
        if (!tenantInfoService.checkAccountLimit(LoginUserUtil.getTenantId())) {
            return err40000("账号数量已达上限，请联系管理员");
        } else if (userInfoService.existByUsername(userReq.getUserId(), userReq.getUsername())) {
            return err40000("修改用户'" + userReq.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(userReq.getPhoneNumber())
                && userInfoService.existByPhoneNumber(userReq.getUserId(), userReq.getPhoneNumber())) {
            return err40000("修改用户'" + userReq.getUsername() + "'失败，手机号码已存在");
        }
        UserInfoDO user = BeanUtil.convert(userReq, UserInfoDO.class);
        String password = RandomStringUtils.secure().next(8);
        user.setPassword(password);

        boolean success = userInfoService.saveUser(user, userReq.getRoleIds());
        if (success) {
            SpringUtil.getContext()
                    .publishEvent(new CreateUserEvent(LoginUserUtil.getId(), userReq.getUsername(), password));
        }
        return toRes(success);
    }

    @Operation(summary = "修改用户")
    @OperateLog(title = "系统用户", subTitle = "修改用户", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckSystemPerms("user-mod:user-mng:edit")
    public Result<Void> edit(@Validated @RequestBody UserManageReq userReq) {
        // 校验权限
        userInfoDataScopeManager.checkDataScopes(userReq.getUserId());
        if (userInfoService.existByUsername(userReq.getUserId(), userReq.getUsername())) {
            return err40000("修改用户'" + userReq.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(userReq.getPhoneNumber())
                && userInfoService.existByPhoneNumber(userReq.getUserId(), userReq.getPhoneNumber())) {
            return err40000("修改用户'" + userReq.getUsername() + "'失败，手机号码已存在");
        }
        UserInfoDO user = BeanUtil.convert(userReq, UserInfoDO.class);

        return toRes(userInfoService.modifyUser(user, userReq.getRoleIds()));
    }

    @Operation(summary = "用户授权角色")
    @OperateLog(title = "系统用户", subTitle = "授权用户角色", operateType = OperateType.GRANT)
    @PostMapping("/grant-role")
    @CheckSystemPerms("user-mod:user-mng:grant-role")
    public Result<Void> grantRoles(@RequestBody @Valid UserToRolesReq req) {
        // 校验用户可操作权限
        userInfoDataScopeManager.checkDataScopes(req.getUserId());
        // 校验角色可操作权限
        roleInfoDataScopeManager.checkDataScopes(req.getRoleIds());
        return toRes(userInfoService.grantRoles(req.getUserId(), req.getRoleIds()));
    }

    @Operation(summary = "删除用户")
    @OperateLog(title = "系统用户", subTitle = "删除用户", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckSystemPerms("user-mod:user-mng:remove")
    public Result<Void> remove(@RequestBody List<Long> userIds) {
        // 校验权限
        userInfoDataScopeManager.checkDataScopes(userIds);
        if (tenantInfoService.checkAdministrator(userIds)) {
            return err40000("删除失败，用户包含租户超级管理员");
        }
        return toRes(userInfoService.removeByIds(userIds));
    }

    @Operation(summary = "重置密码")
    @OperateLog(title = "系统用户", subTitle = "重置密码", operateType = OperateType.UPDATE)
    @PostMapping("/reset-password")
    @CheckSystemPerms("user-mod:user-mng:edit")
    public Result<Void> resetPwd(@RequestBody @Valid ResetPwdReq req) {
        // 校验权限
        userInfoDataScopeManager.checkDataScopes(req.getId());

        UserInfoDO update = new UserInfoDO(req.getId());
        update.setPassword(passwordEncoder.encode(req.getPassword()));
        return toRes(userInfoMapper.updateById(update));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "系统用户", subTitle = "状态修改", operateType = OperateType.UPDATE)
    @PostMapping("/edit-status")
    @CheckSystemPerms("user-mod:user-mng:edit")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        // 校验权限
        userInfoDataScopeManager.checkDataScopes(statusReq.getId());
        UserInfoDO update = new UserInfoDO(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(userInfoMapper.updateById(update));
    }

    @Operation(summary = "导出")
    @OperateLog(title = "系统用户", subTitle = "导出用户数据", operateType = OperateType.EXPORT)
    @GetMapping("/export")
    @CheckSystemPerms("user-mod:user-mng:export")
    public void export(HttpServletResponse response, UserManageQueryReq queryReq) {
        List<UserInfoManageVO> list = userInfoDataScopeManager.listVO(this.buildPageWrapper(queryReq));
        List<UserInfoExport> convert = BeanUtil.convert(list, UserInfoExport.class);
        ExcelUtil.exportExcel(convert, "后台用户数据", UserInfoExport.class, response);
    }

}
