package com.thingslink.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.auth.domain.SysTenantPackage;
import com.thingslink.auth.mapper.SysTenantPackageMapper;
import com.thingslink.auth.service.SysTenantPackageService;
import com.thingslink.common.core.constant.CommonConstants;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.BusinessType;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
@RequestMapping("/tenant/package")
public class SysTenantPackageController extends BaseController {

    private final SysTenantPackageMapper tenantPackageMapper;
    private final SysTenantPackageService tenantPackageService;

    @Operation(summary = "套餐分页")
    @PreAuthorize("hasAuthority('tenantPackage:list')")
    @GetMapping("/list")
    public Result<Page<SysTenantPackage>> list(SysTenantPackage sysTenantPackage) {
        return success(tenantPackageService.listPage(sysTenantPackage));
    }

    @Operation(summary = "套餐下拉选列表")
    @PreAuthorize("hasAuthority('tenantPackage:list')")
    @GetMapping("/selectList")
    public Result<List<SysTenantPackage>> selectList() {
        LambdaQueryWrapper<SysTenantPackage> lqw = new LambdaQueryWrapper<SysTenantPackage>()
                .eq(SysTenantPackage::getStatus, CommonConstants.STATUS_ENABLE);
        return success(tenantPackageMapper.selectList(lqw));
    }

    @Operation(summary = "套餐详细信息")
    @PreAuthorize("hasAuthority('tenantPackage:query')")
    @GetMapping("/{packageId}")
    public Result<SysTenantPackage> getInfo(@NotNull(message = "[packageId] {validate.notnull}")
                                            @PathVariable Long packageId) {
        return success(tenantPackageMapper.selectById(packageId));
    }

    @Operation(summary = "新增租户套餐")
    @PreAuthorize("hasAuthority('tenantPackage:add')")
    @OperateLog(title = "租户套餐", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<Void> add(@Valid @RequestBody SysTenantPackage tenantPackage) {
        return toRes(tenantPackageMapper.insert(tenantPackage));
    }

    @Operation(summary = "修改租户套餐")
    @PreAuthorize("hasAuthority('tenantPackage:edit')")
    @OperateLog(title = "租户套餐", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<Void> edit(@Valid @RequestBody SysTenantPackage tenantPackage) {
        return toRes(tenantPackageMapper.updateById(tenantPackage));
    }

    @Operation(summary = "删除租户套餐")
    @PreAuthorize("hasAuthority('tenantPackage:remove')")
    @OperateLog(title = "租户套餐", businessType = BusinessType.DELETE)
    @DeleteMapping("/{packageIds}")
    public Result<Void> remove(@NotEmpty(message = "[packageId] {validate.notnull}") @PathVariable Long[] packageIds) {
        return toRes(tenantPackageMapper.deleteBatchIds(Arrays.asList(packageIds)));
    }
}
