package com.wzkris.user.controller;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.annotation.IgnoreTenant;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.SysTenantWalletRecord;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.dto.SysTenantDTO;
import com.wzkris.user.domain.req.EditStatusReq;
import com.wzkris.user.domain.req.SysTenantQueryReq;
import com.wzkris.user.domain.req.SysTenantWalletRecordQueryReq;
import com.wzkris.user.domain.vo.SysTenantVO;
import com.wzkris.user.mapper.SysTenantMapper;
import com.wzkris.user.mapper.SysTenantWalletRecordMapper;
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
@PreAuthorize("@SysUtil.isSuperTenant()")// 只允许超级租户访问
@IgnoreTenant// 忽略租户隔离
@RequestMapping("/sys_tenant")
public class SysTenantController extends BaseController {

    private final SysTenantMapper tenantMapper;
    private final SysTenantService tenantService;
    private final SysTenantWalletRecordMapper tenantWalletRecordMapper;
    private final SysUserService userService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "租户分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('tenant:list')")
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
    public Result<Page<SysTenant>> selectlist(String tenantName) {
        startPage();
        LambdaQueryWrapper<SysTenant> lqw = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant::getTenantId, SysTenant::getTenantName)
                .like(StringUtil.isNotBlank(tenantName), SysTenant::getTenantName, tenantName);
        List<SysTenant> list = tenantMapper.selectList(lqw);
        return getDataTable(list);
    }

    @Operation(summary = "ID获取租户详细信息")
    @GetMapping("/{tenantId}")
    @PreAuthorize("@ps.hasPerms('tenant:query')")
    public Result<SysTenant> queryByid(@NotNull(message = "[tenantId] {validate.notnull}")
                                       @PathVariable Long tenantId) {
        return ok(tenantMapper.selectById(tenantId));
    }

    @Operation(summary = "分页获取租户钱包记录")
    @GetMapping("/wallet_record/list")
    @PreAuthorize("@ps.hasPerms('wallet_record:list')")
    public Result<Page<SysTenantWalletRecord>> listWalletPage(SysTenantWalletRecordQueryReq queryReq) {
        startPage();
        List<SysTenantWalletRecord> recordList = tenantWalletRecordMapper.selectList(this.buildWalletQueryWrapper(queryReq));
        return getDataTable(recordList);
    }

    private LambdaQueryWrapper<SysTenantWalletRecord> buildWalletQueryWrapper(SysTenantWalletRecordQueryReq queryReq) {
        return new LambdaQueryWrapper<SysTenantWalletRecord>()
                .eq(StringUtil.isNotNull(queryReq.getTenantId()), SysTenantWalletRecord::getTenantId, queryReq.getTenantId())
                .like(StringUtil.isNotBlank(queryReq.getType()), SysTenantWalletRecord::getType, queryReq.getType())
                .between(queryReq.getParams().get("beginTime") != null && queryReq.getParams().get("endTime") != null,
                        SysTenantWalletRecord::getCreateAt, queryReq.getParams().get("beginTime"), queryReq.getParams().get("endTime"))
                .orderByDesc(SysTenantWalletRecord::getRecordId);
    }

    @Operation(summary = "新增租户")
    @OperateLog(title = "租户管理", subTitle = "新增租户", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('tenant:add')")
    public Result<Void> add(@Validated @RequestBody SysTenantDTO tenantDTO) {
        if (userService.checkUserUnique(new SysUser().setUsername(tenantDTO.getUsername()))) {
            return fail("登录账号'" + tenantDTO.getUsername() + "'已存在");
        }
        tenantService.insertTenant(tenantDTO);
        return ok();
    }

    @Operation(summary = "修改租户")
    @OperateLog(title = "租户管理", subTitle = "修改租户", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('tenant:edit')")
    public Result<Void> edit(@RequestBody SysTenant sysTenant) {
        sysTenant.setAdministrator(null);
        sysTenant.setOperPwd(null);
        return toRes(tenantMapper.updateById(sysTenant));
    }

    @Operation(summary = "修改租户状态")
    @OperateLog(title = "租户管理", subTitle = "修改租户状态", operateType = OperateType.UPDATE)
    @PostMapping("/edit_status")
    @PreAuthorize("@ps.hasPerms('tenant:edit')")
    public Result<Void> editStatus(@RequestBody @Valid EditStatusReq statusReq) {
        SysTenant update = new SysTenant(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(tenantMapper.updateById(update));
    }

    @Operation(summary = "重置租户操作密码")
    @OperateLog(title = "租户管理", subTitle = "重置操作密码", operateType = OperateType.UPDATE)
    @PostMapping("/edit_operpwd")
    @PreAuthorize("@ps.hasPerms('tenant:edit_operpwd')")
    public Result<Void> editOperPwd(@RequestBody SysTenant sysTenant) {
        if (StringUtil.length(sysTenant.getOperPwd()) != 6 || !NumberUtil.isNumber(sysTenant.getOperPwd())) {
            return fail("操作密码必须为6位数字");
        }
        SysTenant update = new SysTenant(sysTenant.getTenantId());
        update.setOperPwd(passwordEncoder.encode(sysTenant.getOperPwd()));
        return toRes(tenantMapper.updateById(update));
    }

    @Operation(summary = "删除租户")
    @OperateLog(title = "租户管理", subTitle = "删除租户", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('tenant:remove')")
    public Result<Void> remove(@RequestBody @NotEmpty(message = "[tenantIds] {validate.notnull}") List<Long> tenantIds) {
        tenantService.deleteByIds(tenantIds);
        return ok();
    }

}
