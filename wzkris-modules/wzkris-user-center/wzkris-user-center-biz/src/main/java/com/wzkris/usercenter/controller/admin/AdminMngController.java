package com.wzkris.usercenter.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.excel.utils.ExcelUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.AdminInfoDO;
import com.wzkris.usercenter.domain.export.admin.AdminInfoExport;
import com.wzkris.usercenter.domain.req.PwdResetReq;
import com.wzkris.usercenter.domain.req.StatusEditReq;
import com.wzkris.usercenter.domain.req.admin.AdminMngAddReq;
import com.wzkris.usercenter.domain.req.admin.AdminMngEditReq;
import com.wzkris.usercenter.domain.req.admin.AdminMngGrantReq;
import com.wzkris.usercenter.domain.req.admin.AdminMngQueryReq;
import com.wzkris.usercenter.domain.resp.CheckedSelectResp;
import com.wzkris.usercenter.domain.resp.SelectResp;
import com.wzkris.usercenter.domain.resp.SelectTreeResp;
import com.wzkris.usercenter.domain.resp.admin.AdminMngResp;
import com.wzkris.usercenter.event.CreateAdminEvent;
import com.wzkris.usercenter.mapper.AdminInfoMapper;
import com.wzkris.usercenter.mapper.DeptInfoMapper;
import com.wzkris.usercenter.mapper.RoleInfoMapper;
import com.wzkris.usercenter.service.AdminInfoService;
import com.wzkris.usercenter.service.RoleInfoService;
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
public class AdminMngController extends BaseController {

    private final AdminInfoMapper adminInfoMapper;

    private final AdminInfoService adminInfoService;

    private final RoleInfoService roleInfoService;

    private final PasswordEncoder passwordEncoder;

    private final DeptInfoMapper deptInfoMapper;

    private final RoleInfoMapper roleInfoMapper;

    @Operation(summary = "管理员分页列表")
    @GetMapping("/page")
    @CheckAdminPerms("user-mod:admin-mng:page")
    public Result<Page<AdminMngResp>> page(AdminMngQueryReq queryReq) {
        startPage();
        List<AdminMngResp> list = adminInfoMapper.selectVOList(this.buildPageWrapper(queryReq));
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

    @Operation(summary = "管理员 - 部门选择树")
    @GetMapping("/dept-selecttree")
    @CheckAdminPerms(
            value = {"user-mod:admin-mng:edit", "user-mod:admin-mng:add"},
            mode = CheckMode.OR)
    public Result<List<SelectTreeResp>> deptSelectTree(String deptName) {
        return ok(deptInfoMapper.selectLists(null).stream()
                .filter(dept -> StringUtil.isBlank(deptName) || dept.getDeptName().contains(deptName))
                .map(SelectTreeResp::new)
                .toList());
    }

    @Operation(summary = "管理员 - 角色选择列表")
    @GetMapping({"/role-checked-select/", "/role-checked-select/{adminId}"})
    @CheckAdminPerms(
            value = {"user-mod:admin-mng:edit", "user-mod:admin-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectResp> roleSelect(@PathVariable(required = false) Long adminId, String roleName) {
        adminInfoMapper.checkDataScopes(adminId);
        CheckedSelectResp checkedSelectResp = new CheckedSelectResp();
        checkedSelectResp.setCheckedKeys(adminId == null ? Collections.emptyList() : roleInfoService.listIdByAdminId(adminId));
        checkedSelectResp.setSelects(roleInfoMapper.selectLists(null).stream()
                .filter(role -> StringUtil.isBlank(roleName) || role.getRoleName().contains(roleName))
                .map(SelectResp::new)
                .toList());
        return ok(checkedSelectResp);
    }

    @Operation(summary = "管理员详细信息")
    @GetMapping("/{adminId}")
    @CheckAdminPerms("user-mod:admin-mng:query")
    public Result<AdminInfoDO> getInfo(@PathVariable Long adminId) {
        adminInfoMapper.checkDataScopes(adminId);
        return ok(adminInfoMapper.selectById(adminId));
    }

    @Operation(summary = "新增管理员")
    @OperateLog(title = "管理员管理", subTitle = "新增管理员", type = OperateTypeEnum.INSERT)
    @PostMapping("/add")
    @CheckAdminPerms("user-mod:admin-mng:add")
    public Result<Void> add(@Validated @RequestBody AdminMngAddReq req) {
        if (adminInfoService.existByUsername(null, req.getUsername())) {
            return requestFail("添加管理员'" + req.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(req.getPhoneNumber())
                && adminInfoService.existByPhoneNumber(null, req.getPhoneNumber())) {
            return requestFail("添加管理员'" + req.getUsername() + "'失败，手机号码已存在");
        }
        AdminInfoDO admin = BeanUtil.convert(req, AdminInfoDO.class);
        String password = RandomStringUtils.secure().nextAlphabetic(8);
        admin.setPassword(password);

        boolean success = adminInfoService.saveAdmin(admin, req.getRoleIds());
        if (success) {
            SpringUtil.getContext()
                    .publishEvent(new CreateAdminEvent(SecurityUtil.getUid(), req.getUsername(), password));
        }
        return toRes(success);
    }

    @Operation(summary = "修改管理员")
    @OperateLog(title = "管理员管理", subTitle = "修改管理员", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit")
    @CheckAdminPerms("user-mod:admin-mng:edit")
    public Result<Void> edit(@Validated @RequestBody AdminMngEditReq req) {
        adminInfoMapper.checkDataScopes(req.getAdminId());
        if (adminInfoService.existByUsername(req.getAdminId(), req.getUsername())) {
            return requestFail("修改管理员'" + req.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(req.getPhoneNumber())
                && adminInfoService.existByPhoneNumber(req.getAdminId(), req.getPhoneNumber())) {
            return requestFail("修改管理员'" + req.getUsername() + "'失败，手机号码已存在");
        }
        AdminInfoDO admin = BeanUtil.convert(req, AdminInfoDO.class);

        return toRes(adminInfoService.modifyAdmin(admin, req.getRoleIds()));
    }

    @Operation(summary = "管理员授权角色")
    @OperateLog(title = "管理员管理", subTitle = "授权管理员角色", type = OperateTypeEnum.GRANT)
    @PostMapping("/grant-role")
    @CheckAdminPerms("user-mod:admin-mng:grant-role")
    public Result<Void> grantRoles(@RequestBody @Valid AdminMngGrantReq req) {
        adminInfoMapper.checkDataScopes(req.getAdminId());
        roleInfoMapper.checkDataScopes(req.getRoleIds());
        return toRes(adminInfoService.grantRoles(req.getAdminId(), req.getRoleIds()));
    }

    @Operation(summary = "删除管理员")
    @OperateLog(title = "管理员管理", subTitle = "删除管理员", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("user-mod:admin-mng:remove")
    public Result<Void> remove(@RequestBody List<Long> userIds) {
        adminInfoMapper.checkDataScopes(userIds);
        return toRes(adminInfoService.removeByIds(userIds));
    }

    @Operation(summary = "重置密码")
    @OperateLog(title = "管理员管理", subTitle = "重置密码", type = OperateTypeEnum.UPDATE)
    @PostMapping("/reset-password")
    @CheckAdminPerms("user-mod:admin-mng:edit")
    public Result<Void> resetPwd(@RequestBody @Valid PwdResetReq req) {
        adminInfoMapper.checkDataScopes(req.getId());
        AdminInfoDO update = new AdminInfoDO(req.getId());
        update.setPassword(passwordEncoder.encode(req.getPassword()));
        return toRes(adminInfoMapper.updateById(update));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "管理员管理", subTitle = "状态修改", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit-status")
    @CheckAdminPerms("user-mod:admin-mng:edit")
    public Result<Void> editStatus(@RequestBody StatusEditReq editReq) {
        adminInfoMapper.checkDataScopes(editReq.getId());
        AdminInfoDO update = new AdminInfoDO(editReq.getId());
        update.setStatus(editReq.getStatus());
        return toRes(adminInfoMapper.updateById(update));
    }

    @Operation(summary = "导出")
    @OperateLog(title = "管理员管理", subTitle = "导出管理员数据", type = OperateTypeEnum.EXPORT)
    @GetMapping("/export")
    @CheckAdminPerms("user-mod:admin-mng:export")
    public void export(HttpServletResponse response, AdminMngQueryReq queryReq) {
        List<AdminMngResp> list = adminInfoMapper.selectVOList(this.buildPageWrapper(queryReq));
        List<AdminInfoExport> convert = BeanUtil.convert(list, AdminInfoExport.class);
        ExcelUtil.exportExcel(convert, "后台管理员数据", AdminInfoExport.class, false, response, null);
    }

}
