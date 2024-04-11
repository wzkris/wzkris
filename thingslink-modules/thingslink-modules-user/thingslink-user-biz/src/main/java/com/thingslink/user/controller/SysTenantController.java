package com.thingslink.user.controller;

import com.thingslink.user.domain.SysTenant;
import com.thingslink.user.domain.SysUser;
import com.thingslink.user.domain.dto.SysTenantDTO;
import com.thingslink.user.mapper.SysTenantMapper;
import com.thingslink.user.service.SysTenantService;
import com.thingslink.user.service.SysUserService;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.annotation.group.ValidationGroups;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.OperateType;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.orm.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 租户管理
 *
 * @author wzkris
 */
@Tag(name = "租户管理")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/tenant")
public class SysTenantController extends BaseController {

    private final SysTenantMapper sysTenantMapper;
    private final SysTenantService sysTenantService;
    private final SysUserService sysUserService;

    @Operation(summary = "租户分页")
    @PreAuthorize("hasAuthority('tenant:list')")
    @GetMapping("/list")
    public Result<Page<SysTenant>> list(SysTenant sysTenant) {
        return success(sysTenantService.listPage(sysTenant));
    }

    @Operation(summary = "获取租户详细信息")
    @PreAuthorize("hasAuthority('tenant:query')")
    @GetMapping("/{tenantId}")
    public Result<SysTenant> getInfo(@NotNull(message = "[tenantId] {validate.notnull}")
                                     @PathVariable Long tenantId) {
        return success(sysTenantMapper.selectById(tenantId));
    }

    @Operation(summary = "新增租户")
    @PreAuthorize("hasAuthority('tenant:add')")
    @OperateLog(title = "租户", operateType = OperateType.INSERT)
    @PostMapping
    public Result<Void> add(@Validated(value = ValidationGroups.Insert.class) @RequestBody SysTenantDTO sysTenantDTO) {
        if (sysUserService.checkUserUnique(new SysUser().setUsername(sysTenantDTO.getUsername()), null)) {
            return fail("登录账号'" + sysTenantDTO.getUsername() + "'已存在");
        }
        return toRes(sysTenantService.insertTenant(sysTenantDTO));
    }

    @Operation(summary = "修改租户")
    @PreAuthorize("hasAuthority('tenant:edit')")
    @OperateLog(title = "租户", operateType = OperateType.UPDATE)
    @PutMapping
    public Result<Void> edit(@RequestBody SysTenant sysTenant) {
        return toRes(sysTenantService.updateTenant(sysTenant));
    }

    @Operation(summary = "删除租户")
    @PreAuthorize("hasAuthority('tenant:remove')")
    @OperateLog(title = "租户", operateType = OperateType.DELETE)
    @DeleteMapping("/{tenantIds}")
    public Result<Void> remove(@NotEmpty(message = "[tenantIds] {validate.notnull}") @PathVariable Long[] tenantIds) {
        return toRes(sysTenantMapper.deleteBatchIds(Arrays.asList(tenantIds)));
    }

}
