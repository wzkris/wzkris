package com.wzkris.principal.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.excel.utils.ExcelUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.common.security.annotation.enums.CheckMode;
import com.wzkris.common.security.utils.AdminUtil;
import com.wzkris.common.validator.group.ValidationGroups;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.principal.domain.AdminInfoDO;
import com.wzkris.principal.domain.export.admin.AdminInfoExport;
import com.wzkris.principal.domain.req.EditStatusReq;
import com.wzkris.principal.domain.req.ResetPwdReq;
import com.wzkris.principal.domain.req.admin.AdminMngQueryReq;
import com.wzkris.principal.domain.req.admin.AdminMngReq;
import com.wzkris.principal.domain.req.admin.AdminToRolesReq;
import com.wzkris.principal.domain.vo.CheckedSelectVO;
import com.wzkris.principal.domain.vo.SelectTreeVO;
import com.wzkris.principal.domain.vo.admin.AdminManageVO;
import com.wzkris.principal.listener.event.CreateUserEvent;
import com.wzkris.principal.manager.AdminInfoDscManager;
import com.wzkris.principal.manager.DeptInfoDscManager;
import com.wzkris.principal.manager.RoleInfoDscManager;
import com.wzkris.principal.mapper.AdminInfoMapper;
import com.wzkris.principal.service.AdminInfoService;
import com.wzkris.principal.service.RoleInfoService;
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
 * 管理员管理
 *
 * @author wzkris
 */
@Tag(name = "管理员管理")
@Validated
@RestController
@RequestMapping("/admin-manage")
@RequiredArgsConstructor
public class AdminInfoMngController extends BaseController {

    private final AdminInfoMapper adminInfoMapper;

    private final AdminInfoService adminInfoService;

    private final RoleInfoService roleInfoService;

    private final PasswordEncoder passwordEncoder;

    private final AdminInfoDscManager adminInfoDscManager;

    private final DeptInfoDscManager deptInfoDscManager;

    private final RoleInfoDscManager roleInfoDscManager;

    @Operation(summary = "管理员分页列表")
    @GetMapping("/list")
    @CheckUserPerms("prin-mod:admin-mng:list")
    public Result<Page<AdminManageVO>> listPage(AdminMngQueryReq queryReq) {
        startPage();
        List<AdminManageVO> list = adminInfoDscManager.listVO(this.buildPageWrapper(queryReq));
        return getDataTable(list);
    }

    private QueryWrapper<AdminInfoDO> buildPageWrapper(AdminMngQueryReq queryReq) {
        return new QueryWrapper<AdminInfoDO>()
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
                .orderByDesc("u.admin_id");
    }

    @Operation(summary = "管理员-部门选择树")
    @GetMapping("/dept-selecttree")
    @CheckUserPerms(
            value = {"prin-mod:admin-mng:edit", "prin-mod:admin-mng:add"},
            mode = CheckMode.OR)
    public Result<List<SelectTreeVO>> deptSelectTree(String deptName) {
        return ok(deptInfoDscManager.listSelectTree(deptName));
    }

    @Operation(summary = "管理员-角色选择列表")
    @GetMapping({"/role-checked-select/", "/role-checked-select/{adminId}"})
    @CheckUserPerms(
            value = {"prin-mod:admin-mng:edit", "prin-mod:admin-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectVO> roleSelect(@PathVariable(required = false) Long adminId, String roleName) {
        adminInfoDscManager.checkDataScopes(adminId);
        CheckedSelectVO checkedSelectVO = new CheckedSelectVO();
        checkedSelectVO.setCheckedKeys(adminId == null ? Collections.emptyList() : roleInfoService.listIdByAdminId(adminId));
        checkedSelectVO.setSelects(roleInfoDscManager.listSelect(roleName));
        return ok(checkedSelectVO);
    }

    @Operation(summary = "管理员详细信息")
    @GetMapping("/{adminId}")
    @CheckUserPerms("prin-mod:admin-mng:query")
    public Result<AdminInfoDO> getInfo(@PathVariable Long adminId) {
        // 校验权限
        adminInfoDscManager.checkDataScopes(adminId);
        return ok(adminInfoMapper.selectById(adminId));
    }

    @Operation(summary = "新增管理员")
    @OperateLog(title = "管理员管理", subTitle = "新增管理员", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckUserPerms("prin-mod:admin-mng:add")
    public Result<Void> add(@Validated(ValidationGroups.Insert.class) @RequestBody AdminMngReq userReq) {
        if (adminInfoService.existByUsername(userReq.getAdminId(), userReq.getUsername())) {
            return err40000("添加管理员'" + userReq.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(userReq.getPhoneNumber())
                && adminInfoService.existByPhoneNumber(userReq.getAdminId(), userReq.getPhoneNumber())) {
            return err40000("添加管理员'" + userReq.getUsername() + "'失败，手机号码已存在");
        }
        AdminInfoDO admin = BeanUtil.convert(userReq, AdminInfoDO.class);
        String password = RandomStringUtils.secure().nextAlphabetic(8);
        admin.setPassword(password);

        boolean success = adminInfoService.saveAdmin(admin, userReq.getRoleIds());
        if (success) {
            SpringUtil.getContext()
                    .publishEvent(new CreateUserEvent(AdminUtil.getId(), userReq.getUsername(), password));
        }
        return toRes(success);
    }

    @Operation(summary = "修改管理员")
    @OperateLog(title = "管理员管理", subTitle = "修改管理员", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckUserPerms("prin-mod:admin-mng:edit")
    public Result<Void> edit(@Validated @RequestBody AdminMngReq userReq) {
        // 校验权限
        adminInfoDscManager.checkDataScopes(userReq.getAdminId());
        if (adminInfoService.existByUsername(userReq.getAdminId(), userReq.getUsername())) {
            return err40000("修改管理员'" + userReq.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(userReq.getPhoneNumber())
                && adminInfoService.existByPhoneNumber(userReq.getAdminId(), userReq.getPhoneNumber())) {
            return err40000("修改管理员'" + userReq.getUsername() + "'失败，手机号码已存在");
        }
        AdminInfoDO admin = BeanUtil.convert(userReq, AdminInfoDO.class);

        return toRes(adminInfoService.modifyAdmin(admin, userReq.getRoleIds()));
    }

    @Operation(summary = "管理员授权角色")
    @OperateLog(title = "管理员管理", subTitle = "授权管理员角色", operateType = OperateType.GRANT)
    @PostMapping("/grant-role")
    @CheckUserPerms("prin-mod:admin-mng:grant-role")
    public Result<Void> grantRoles(@RequestBody @Valid AdminToRolesReq req) {
        // 校验管理员可操作权限
        adminInfoDscManager.checkDataScopes(req.getAdminId());
        // 校验角色可操作权限
        roleInfoDscManager.checkDataScopes(req.getRoleIds());
        return toRes(adminInfoService.grantRoles(req.getAdminId(), req.getRoleIds()));
    }

    @Operation(summary = "删除管理员")
    @OperateLog(title = "管理员管理", subTitle = "删除管理员", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckUserPerms("prin-mod:admin-mng:remove")
    public Result<Void> remove(@RequestBody List<Long> userIds) {
        // 校验权限
        adminInfoDscManager.checkDataScopes(userIds);
        return toRes(adminInfoService.removeByIds(userIds));
    }

    @Operation(summary = "重置密码")
    @OperateLog(title = "管理员管理", subTitle = "重置密码", operateType = OperateType.UPDATE)
    @PostMapping("/reset-password")
    @CheckUserPerms("prin-mod:admin-mng:edit")
    public Result<Void> resetPwd(@RequestBody @Valid ResetPwdReq req) {
        // 校验权限
        adminInfoDscManager.checkDataScopes(req.getId());

        AdminInfoDO update = new AdminInfoDO(req.getId());
        update.setPassword(passwordEncoder.encode(req.getPassword()));
        return toRes(adminInfoMapper.updateById(update));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "管理员管理", subTitle = "状态修改", operateType = OperateType.UPDATE)
    @PostMapping("/edit-status")
    @CheckUserPerms("prin-mod:admin-mng:edit")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        // 校验权限
        adminInfoDscManager.checkDataScopes(statusReq.getId());
        AdminInfoDO update = new AdminInfoDO(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(adminInfoMapper.updateById(update));
    }

    @Operation(summary = "导出")
    @OperateLog(title = "管理员管理", subTitle = "导出管理员数据", operateType = OperateType.EXPORT)
    @GetMapping("/export")
    @CheckUserPerms("prin-mod:admin-mng:export")
    public void export(HttpServletResponse response, AdminMngQueryReq queryReq) {
        List<AdminManageVO> list = adminInfoDscManager.listVO(this.buildPageWrapper(queryReq));
        List<AdminInfoExport> convert = BeanUtil.convert(list, AdminInfoExport.class);
        ExcelUtil.exportExcel(convert, "后台管理员数据", AdminInfoExport.class, response);
    }

}
