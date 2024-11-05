package com.wzkris.user.controller;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.annotation.DynamicTenant;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.page.Page;
import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.dto.SysTenantDTO;
import com.wzkris.user.mapper.SysTenantMapper;
import com.wzkris.user.service.SysTenantService;
import com.wzkris.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@DynamicTenant(enableIgnore = "true")// 忽略租户隔离
@RequestMapping("/sys_tenant")
public class SysTenantController extends BaseController {

    private final PasswordEncoder passwordEncoder;
    private final SysTenantMapper tenantMapper;
    private final SysTenantService tenantService;
    private final SysUserService userService;

    @Operation(summary = "租户分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('tenant:list')")
    public Result<Page<SysTenant>> listPage(SysTenant sysTenant) {
        startPage();
        List<SysTenant> list = tenantService.list(sysTenant);
        return getDataTable(list);
    }

    @Operation(summary = "租户选择列表(带分页)")
    @GetMapping("/selectlist")
    @PreAuthorize("@ps.hasPerms('tenant:list')")
    public Result<Page<SysTenant>> selectlist(SysTenant sysTenant) {
        startPage();
        LambdaQueryWrapper<SysTenant> lqw = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant::getTenantId, SysTenant::getTenantName)
                .like(StringUtil.isNotBlank(sysTenant.getTenantName()), SysTenant::getTenantName, sysTenant.getTenantName());
        List<SysTenant> list = tenantMapper.selectList(lqw);
        return getDataTable(list);
    }

    @Operation(summary = "ID获取租户详细信息")
    @GetMapping("/{tenantId}")
    @PreAuthorize("@ps.hasPerms('tenant:query')")
    public Result<SysTenant> queryid(@NotNull(message = "[tenantId] {validate.notnull}")
                                     @PathVariable Long tenantId) {
        return ok(tenantMapper.selectById(tenantId));
    }

    @Operation(summary = "新增租户")
    @OperateLog(title = "租户管理", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('tenant:add')")
    public Result<Void> add(@Validated @RequestBody SysTenantDTO sysTenantDTO) {
        if (userService.checkUserUnique(new SysUser().setUsername(sysTenantDTO.getUsername()))) {
            return fail("登录账号'" + sysTenantDTO.getUsername() + "'已存在");
        }
        tenantService.insertTenant(sysTenantDTO);
        return ok();
    }

    @Operation(summary = "修改租户")
    @OperateLog(title = "租户管理", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('tenant:edit')")
    public Result<Void> edit(@RequestBody SysTenant sysTenant) {
        sysTenant.setAdministrator(null);
        sysTenant.setOperPwd(null);
        return toRes(tenantMapper.updateById(sysTenant));
    }

    @Operation(summary = "修改租户操作密码")
    @OperateLog(title = "租户管理", operateType = OperateType.UPDATE)
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
    @OperateLog(title = "租户管理", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('tenant:remove')")
    public Result<Void> remove(@RequestBody @NotEmpty(message = "[tenantIds] {validate.notnull}") List<Long> tenantIds) {
        return toRes(tenantMapper.deleteByIds(tenantIds));// soft delete
    }

}
