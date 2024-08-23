package com.wzkris.user.controller;

import com.wzkris.common.core.annotation.group.ValidationGroups;
import com.wzkris.common.core.domain.Result;
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
@DynamicTenant// 超级租户需要看到所有租户信息
@RequestMapping("/sys_tenant")
public class SysTenantController extends BaseController {

    private final SysTenantMapper sysTenantMapper;
    private final SysTenantService sysTenantService;
    private final SysUserService sysUserService;

    @Operation(summary = "租户分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('tenant:list') && @SysUtil.isSuperTenant()")
    public Result<Page<SysTenant>> list(SysTenant sysTenant) {
        startPage();
        List<SysTenant> list = sysTenantService.listPage(sysTenant);
        return getDataTable(list);
    }

    @Operation(summary = "ID获取租户详细信息")
    @GetMapping("/{tenantId}")
    @PreAuthorize("@ps.hasPerms('tenant:query') && @SysUtil.isSuperTenant()")
    public Result<SysTenant> queryid(@NotNull(message = "[tenantId] {validate.notnull}")
                                     @PathVariable Long tenantId) {
        return success(sysTenantMapper.selectById(tenantId));
    }

    @Operation(summary = "新增租户")
    @OperateLog(title = "租户管理", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('tenant:add') && @SysUtil.isSuperTenant()")
    public Result<Void> add(@Validated(value = ValidationGroups.Insert.class) @RequestBody SysTenantDTO sysTenantDTO) {
        if (sysUserService.checkUserUnique(new SysUser().setUsername(sysTenantDTO.getUsername()))) {
            return fail("登录账号'" + sysTenantDTO.getUsername() + "'已存在");
        }
        return toRes(sysTenantService.insertTenant(sysTenantDTO));
    }

    @Operation(summary = "修改租户")
    @OperateLog(title = "租户管理", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('tenant:edit') && @SysUtil.isSuperTenant()")
    public Result<Void> edit(@RequestBody SysTenant sysTenant) {
        return toRes(sysTenantService.updateTenant(sysTenant));
    }

    @Operation(summary = "删除租户")
    @OperateLog(title = "租户管理", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('tenant:remove') && @SysUtil.isSuperTenant()")
    public Result<Void> remove(@RequestBody @NotEmpty(message = "[tenantIds] {validate.notnull}") List<Long> tenantIds) {
        return toRes(sysTenantMapper.deleteByIds(tenantIds));
    }

}
