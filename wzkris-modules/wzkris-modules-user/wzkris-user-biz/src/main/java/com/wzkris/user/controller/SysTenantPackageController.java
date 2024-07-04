package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.page.Page;
import com.wzkris.user.domain.SysTenantPackage;
import com.wzkris.user.mapper.SysTenantPackageMapper;
import com.wzkris.user.service.SysTenantPackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户套餐管理
 *
 * @author Michelle.Chung
 */
@Tag(name = "租户套餐管理")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys_tenant/package")
public class SysTenantPackageController extends BaseController {

    private final SysTenantPackageMapper tenantPackageMapper;
    private final SysTenantPackageService tenantPackageService;

    @Operation(summary = "套餐分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('tenantPackage:list')")
    public Result<Page<SysTenantPackage>> list(SysTenantPackage sysTenantPackage) {
        return success(tenantPackageService.listPage(sysTenantPackage));
    }

    @Operation(summary = "套餐下拉选列表")
    @GetMapping("/selectList")
    @PreAuthorize("@ps.hasPerms('tenantPackage:list')")
    public Result<List<SysTenantPackage>> selectList() {
        LambdaQueryWrapper<SysTenantPackage> lqw = new LambdaQueryWrapper<SysTenantPackage>()
                .eq(SysTenantPackage::getStatus, CommonConstants.STATUS_ENABLE);
        return success(tenantPackageMapper.selectList(lqw));
    }

    @Operation(summary = "套餐详细信息")
    @GetMapping("/{packageId}")
    @PreAuthorize("@ps.hasPerms('tenantPackage:query')")
    public Result<SysTenantPackage> getInfo(@NotNull(message = "[packageId] {validate.notnull}")
                                            @PathVariable Long packageId) {
        return success(tenantPackageMapper.selectById(packageId));
    }

    @Operation(summary = "新增租户套餐")
    @OperateLog(title = "租户套餐", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('tenantPackage:add')")
    public Result<Void> add(@Valid @RequestBody SysTenantPackage tenantPackage) {
        return toRes(tenantPackageMapper.insert(tenantPackage));
    }

    @Operation(summary = "修改租户套餐")
    @OperateLog(title = "租户套餐", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('tenantPackage:edit')")
    public Result<Void> edit(@Valid @RequestBody SysTenantPackage tenantPackage) {
        return toRes(tenantPackageMapper.updateById(tenantPackage));
    }

    @Operation(summary = "删除租户套餐")
    @OperateLog(title = "租户套餐", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('tenantPackage:remove')")
    public Result<Void> remove(@NotEmpty(message = "[packageId] {validate.notnull}") @RequestBody List<Long> packageIds) {
        return toRes(tenantPackageMapper.deleteByIds(packageIds));
    }
}
