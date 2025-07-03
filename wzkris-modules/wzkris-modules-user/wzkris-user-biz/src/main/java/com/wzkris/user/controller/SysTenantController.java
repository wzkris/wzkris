package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.annotation.group.ValidationGroups;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.annotation.IgnoreTenant;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.common.security.annotation.CheckSystemPerms;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.req.EditStatusReq;
import com.wzkris.user.domain.req.ResetPwdReq;
import com.wzkris.user.domain.req.SysTenantQueryReq;
import com.wzkris.user.domain.req.SysTenantReq;
import com.wzkris.user.domain.vo.SelectVO;
import com.wzkris.user.domain.vo.SysTenantVO;
import com.wzkris.user.listener.event.CreateTenantEvent;
import com.wzkris.user.mapper.SysTenantMapper;
import com.wzkris.user.service.SysTenantPackageService;
import com.wzkris.user.service.SysTenantService;
import com.wzkris.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户管理
 *
 * @author wzkris
 */
@Tag(name = "租户管理")
@Validated
@RequiredArgsConstructor
@RestController
@PreAuthorize("@su.isSuperTenant()") // 只允许超级租户访问
@IgnoreTenant // 忽略租户隔离
@RequestMapping("/sys_tenant")
public class SysTenantController extends BaseController {

    private final SysTenantMapper tenantMapper;

    private final SysTenantService tenantService;

    private final SysUserService userService;

    private final SysTenantPackageService tenantPackageService;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "租户分页")
    @GetMapping("/list")
    @CheckSystemPerms("sys_tenant:list")
    public Result<Page<SysTenantVO>> listPage(SysTenantQueryReq queryReq) {
        startPage();
        List<SysTenantVO> list = tenantMapper.selectVOList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private QueryWrapper<SysTenant> buildQueryWrapper(SysTenantQueryReq queryReq) {
        return new QueryWrapper<SysTenant>()
                .like(StringUtil.isNotEmpty(queryReq.getTenantName()), "tenant_name", queryReq.getTenantName())
                .eq(StringUtil.isNotEmpty(queryReq.getStatus()), "t.status", queryReq.getStatus())
                .orderByDesc("t.tenant_id");
    }

    @Operation(summary = "租户选择列表(带分页)")
    @GetMapping("/selectlist")
    public Result<Page<SelectVO>> selectlist(String tenantName) {
        startPage();
        List<SelectVO> list = tenantService.listSelect(tenantName);
        return getDataTable(list);
    }

    @Operation(summary = "套餐选择列表")
    @GetMapping("/package_select")
    @CheckSystemPerms(
            value = {"sys_tenant:add", "sys_tenant:edit"},
            mode = CheckPerms.Mode.OR)
    public Result<List<SelectVO>> packageSelect(String packageName) {
        List<SelectVO> selectVOS = tenantPackageService.listSelect(packageName);
        return ok(selectVOS);
    }

    @Operation(summary = "ID获取租户详细信息")
    @GetMapping("/{tenantId}")
    @CheckSystemPerms("sys_tenant:query")
    public Result<SysTenant> queryByid(
            @NotNull(message = "{desc.tenant}{desc.id}{validate.notnull}") @PathVariable Long tenantId) {
        return ok(tenantMapper.selectById(tenantId));
    }

    @Operation(summary = "新增租户")
    @OperateLog(title = "租户管理", subTitle = "新增租户", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckSystemPerms("sys_tenant:add")
    public Result<Void> add(@Validated(ValidationGroups.Insert.class) @RequestBody SysTenantReq tenantReq) {
        if (userService.checkExistByUsername(null, tenantReq.getUsername())) {
            return err412("登录账号'" + tenantReq.getUsername() + "'已存在");
        }
        SysTenant tenant = BeanUtil.convert(tenantReq, SysTenant.class);

        String operPwd = StringUtil.toStringOrNull(RandomUtils.secure().randomInt(100_000, 999_999));
        tenant.setOperPwd(operPwd);

        String password = RandomStringUtils.secure().next(8);
        boolean success = tenantService.insertTenant(tenant, tenantReq.getUsername(), password);
        if (success) {
            SpringUtil.getContext()
                    .publishEvent(new CreateTenantEvent(
                            SystemUserUtil.getUserId(),
                            tenantReq.getUsername(),
                            tenantReq.getTenantName(),
                            password,
                            operPwd));
        }
        return toRes(success);
    }

    @Operation(summary = "修改租户")
    @OperateLog(title = "租户管理", subTitle = "修改租户", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckSystemPerms("sys_tenant:edit")
    public Result<Void> edit(@Validated @RequestBody SysTenantReq tenantReq) {
        tenantService.checkDataScope(tenantReq.getTenantId());

        SysTenant tenant = BeanUtil.convert(tenantReq, SysTenant.class);
        tenant.setAdministrator(null);
        tenant.setOperPwd(null);
        return toRes(tenantMapper.updateById(tenant));
    }

    @Operation(summary = "修改租户状态")
    @OperateLog(title = "租户管理", subTitle = "修改租户状态", operateType = OperateType.UPDATE)
    @PostMapping("/edit_status")
    @CheckSystemPerms("sys_tenant:edit")
    public Result<Void> editStatus(@RequestBody @Valid EditStatusReq statusReq) {
        tenantService.checkDataScope(statusReq.getId());

        SysTenant update = new SysTenant(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(tenantMapper.updateById(update));
    }

    @Operation(summary = "重置租户操作密码")
    @OperateLog(title = "租户管理", subTitle = "重置操作密码", operateType = OperateType.UPDATE)
    @PostMapping("/reset_operpwd")
    @CheckSystemPerms("sys_tenant:reset_operpwd")
    public Result<Void> resetOperPwd(@RequestBody ResetPwdReq req) {
        tenantService.checkDataScope(req.getId());

        if (StringUtil.length(req.getPassword()) != 6 || !NumberUtils.isCreatable(req.getPassword())) {
            return err412("操作密码必须为6位数字");
        }
        SysTenant update = new SysTenant(req.getId());
        update.setOperPwd(passwordEncoder.encode(req.getPassword()));
        return toRes(tenantMapper.updateById(update));
    }

    @Operation(summary = "删除租户")
    @OperateLog(title = "租户管理", subTitle = "删除租户", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckSystemPerms("sys_tenant:remove")
    public Result<Void> remove(
            @RequestBody @NotNull(message = "{desc.tenant}{desc.id}{validate.notnull}") Long tenantId) {
        tenantService.checkDataScope(tenantId);

        return toRes(tenantService.deleteById(tenantId));
    }

}
