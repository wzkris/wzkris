package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.page.Page;
import com.wzkris.user.domain.SysMenu;
import com.wzkris.user.domain.SysTenantPackage;
import com.wzkris.user.mapper.SysMenuMapper;
import com.wzkris.user.mapper.SysTenantPackageMapper;
import com.wzkris.user.service.SysMenuService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 租户套餐管理
 *
 * @author Michelle.Chung
 */
@Tag(name = "租户套餐管理")
@Validated
@RequiredArgsConstructor
@PreAuthorize("@SysUtil.isSuperTenant()")// 只允许超级租户访问
@RestController
@RequestMapping("/sys_tenant/package")
public class SysTenantPackageController extends BaseController {

    private final SysTenantPackageMapper tenantPackageMapper;
    private final SysTenantPackageService tenantPackageService;
    private final SysMenuMapper sysMenuMapper;
    private final SysMenuService sysMenuService;

    @Operation(summary = "套餐分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('tenant_package:list')")
    public Result<Page<SysTenantPackage>> list(SysTenantPackage sysTenantPackage) {
        return success(tenantPackageService.listPage(sysTenantPackage));
    }

    @Operation(summary = "套餐选择列表")
    @GetMapping("/selectlist")
    @PreAuthorize("@ps.hasPerms('tenant_package:list')")
    public Result<List<SysTenantPackage>> selectList() {
        LambdaQueryWrapper<SysTenantPackage> lqw = new LambdaQueryWrapper<SysTenantPackage>()
                .eq(SysTenantPackage::getStatus, CommonConstants.STATUS_ENABLE);
        return success(tenantPackageMapper.selectList(lqw));
    }

    @Operation(summary = "套餐菜单选择树")
    @GetMapping({"/menu_select_tree/", "/menu_select_tree/{packageId}"})
    @PreAuthorize("@ps.hasPerms('tenant_package:list')")
    public Result<?> tenantPackageMenuTreeList(@PathVariable(required = false) Long packageId) {
        Map<String, Object> res = new HashMap<>(2);
        res.put("checkedKeys", tenantPackageMapper.listMenuIdByPackageId(packageId));
        SysMenu sysMenu = new SysMenu(CommonConstants.STATUS_ENABLE);
        res.put("menus", sysMenuService.listMenuSelectTree(sysMenu));// 查询租户专用菜单
        return success(res);
    }

    @Operation(summary = "套餐详细信息")
    @GetMapping("/{packageId}")
    @PreAuthorize("@ps.hasPerms('tenant_package:query')")
    public Result<SysTenantPackage> getInfo(@NotNull(message = "[packageId] {validate.notnull}")
                                            @PathVariable Long packageId) {
        return success(tenantPackageMapper.selectById(packageId));
    }

    @Operation(summary = "新增租户套餐")
    @OperateLog(title = "租户套餐", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('tenant_package:add')")
    public Result<Void> add(@Valid @RequestBody SysTenantPackage tenantPackage) {
        return toRes(tenantPackageMapper.insert(tenantPackage));
    }

    @Operation(summary = "修改租户套餐")
    @OperateLog(title = "租户套餐", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('tenant_package:edit')")
    public Result<Void> edit(@Valid @RequestBody SysTenantPackage tenantPackage) {
        return toRes(tenantPackageMapper.updateById(tenantPackage));
    }

    @Operation(summary = "删除租户套餐")
    @OperateLog(title = "租户套餐", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('tenant_package:remove')")
    public Result<Void> remove(@NotEmpty(message = "[packageId] {validate.notnull}") @RequestBody List<Long> packageIds) {
        return toRes(tenantPackageMapper.deleteByIds(packageIds));
    }

}
