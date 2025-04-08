package com.wzkris.user.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.annotation.group.ValidationGroups;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.annotation.IgnoreTenant;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.security.oauth2.annotation.CheckPerms;
import com.wzkris.common.security.oauth2.annotation.CheckSystemPerms;
import com.wzkris.common.security.utils.LoginUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.SysTenantWalletRecord;
import com.wzkris.user.domain.req.*;
import com.wzkris.user.domain.vo.SelectVO;
import com.wzkris.user.domain.vo.SysTenantVO;
import com.wzkris.user.listener.event.CreateTenantEvent;
import com.wzkris.user.mapper.SysTenantMapper;
import com.wzkris.user.mapper.SysTenantWalletRecordMapper;
import com.wzkris.user.service.SysTenantPackageService;
import com.wzkris.user.service.SysTenantService;
import com.wzkris.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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
@PreAuthorize("@lg.isSuperTenant()")// 只允许超级租户访问
@IgnoreTenant// 忽略租户隔离
@RequestMapping("/sys_tenant")
public class SysTenantController extends BaseController {

    private final SysTenantMapper tenantMapper;

    private final SysTenantService tenantService;

    private final SysTenantWalletRecordMapper tenantWalletRecordMapper;

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
                .like(StringUtil.isNotNull(queryReq.getTenantName()), "tenant_name", queryReq.getTenantName())
                .eq(StringUtil.isNotNull(queryReq.getStatus()), "t.status", queryReq.getStatus())
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
    @CheckSystemPerms(value = {"sys_tenant:add", "sys_tenant:edit"}, mode = CheckPerms.Mode.OR)
    public Result<List<SelectVO>> packageSelect(String packageName) {
        List<SelectVO> selectVOS = tenantPackageService.listSelect(packageName);
        return ok(selectVOS);
    }

    @Operation(summary = "ID获取租户详细信息")
    @GetMapping("/{tenantId}")
    @CheckSystemPerms("sys_tenant:query")
    public Result<SysTenant> queryByid(@NotNull(message = "{desc.tenant}{desc.id}{validate.notnull}")
                                       @PathVariable Long tenantId) {
        return ok(tenantMapper.selectById(tenantId));
    }

    @Operation(summary = "分页获取租户钱包记录")
    @GetMapping("/wallet_record/list")
    @CheckSystemPerms("sys_tenant:wallet_record:list")
    public Result<Page<SysTenantWalletRecord>> listWalletPage(SysTenantWalletRecordQueryReq queryReq) {
        startPage();
        List<SysTenantWalletRecord> recordList = tenantWalletRecordMapper.selectList(this.buildWalletQueryWrapper(queryReq));
        return getDataTable(recordList);
    }

    private LambdaQueryWrapper<SysTenantWalletRecord> buildWalletQueryWrapper(SysTenantWalletRecordQueryReq queryReq) {
        return new LambdaQueryWrapper<SysTenantWalletRecord>()
                .eq(StringUtil.isNotNull(queryReq.getTenantId()), SysTenantWalletRecord::getTenantId, queryReq.getTenantId())
                .like(StringUtil.isNotBlank(queryReq.getRecordType()), SysTenantWalletRecord::getRecordType, queryReq.getRecordType())
                .between(queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        SysTenantWalletRecord::getCreateAt, queryReq.getParam("beginTime"), queryReq.getParam("endTime"))
                .orderByDesc(SysTenantWalletRecord::getRecordId);
    }

    @Operation(summary = "新增租户")
    @OperateLog(title = "租户管理", subTitle = "新增租户", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckSystemPerms("sys_tenant:add")
    public Result<Void> add(@Validated(ValidationGroups.Insert.class) @RequestBody SysTenantReq tenantReq) {
        if (userService.checkUsedByUsername(null, tenantReq.getUsername())) {
            return error412("登录账号'" + tenantReq.getUsername() + "'已存在");
        }
        SysTenant tenant = BeanUtil.convert(tenantReq, SysTenant.class);

        String operPwd = RandomUtil.randomNumbers(6);
        tenant.setOperPwd(operPwd);

        String password = RandomUtil.randomNumbers(8);
        boolean success = tenantService.insertTenant(tenant, tenantReq.getUsername(), password);
        if (success) {
            SpringUtil.getContext().publishEvent(
                    new CreateTenantEvent(LoginUtil.getUserId(), tenantReq.getUsername(), tenantReq.getTenantName(), password, operPwd)
            );
        }
        return toRes(success);
    }

    @Operation(summary = "修改租户")
    @OperateLog(title = "租户管理", subTitle = "修改租户", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckSystemPerms("sys_tenant:edit")
    public Result<Void> edit(@Validated @RequestBody SysTenantReq tenantReq) {
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
        SysTenant update = new SysTenant(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(tenantMapper.updateById(update));
    }

    @Operation(summary = "重置租户操作密码")
    @OperateLog(title = "租户管理", subTitle = "重置操作密码", operateType = OperateType.UPDATE)
    @PostMapping("/reset_operpwd")
    @CheckSystemPerms("sys_tenant:reset_operpwd")
    public Result<Void> resetOperPwd(@RequestBody ResetPwdReq req) {
        if (StringUtil.length(req.getPassword()) != 6 || !NumberUtil.isNumber(req.getPassword())) {
            return error412("操作密码必须为6位数字");
        }
        SysTenant update = new SysTenant(req.getId());
        update.setOperPwd(passwordEncoder.encode(req.getPassword()));
        return toRes(tenantMapper.updateById(update));
    }

    @Operation(summary = "删除租户")
    @OperateLog(title = "租户管理", subTitle = "删除租户", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckSystemPerms("sys_tenant:remove")
    public Result<Void> remove(@RequestBody @NotEmpty(message = "{desc.tenant}{desc.id}{validate.notnull}") List<Long> tenantIds) {
        tenantService.deleteByIds(tenantIds);
        return ok();
    }

}
