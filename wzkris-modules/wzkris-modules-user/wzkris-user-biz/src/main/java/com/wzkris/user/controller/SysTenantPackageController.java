package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.SysMenu;
import com.wzkris.user.domain.SysTenantPackage;
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
    private final SysMenuService menuService;

    @Operation(summary = "套餐分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('tenant_package:list')")
    public Result<Page<SysTenantPackage>> listPage(SysTenantPackage sysTenantPackage) {
        startPage();
        List<SysTenantPackage> list = tenantPackageService.list(sysTenantPackage);
        return getDataTable(list);
    }

    @Operation(summary = "套餐选择列表(不带分页)")
    @GetMapping("/selectlist")
    @PreAuthorize("@ps.hasPerms('tenant_package:list')")
    public Result<List<SysTenantPackage>> selectList(SysTenantPackage sysTenantPackage) {
        LambdaQueryWrapper<SysTenantPackage> lqw = new LambdaQueryWrapper<SysTenantPackage>()
                .select(SysTenantPackage::getPackageId, SysTenantPackage::getPackageName)
                .eq(SysTenantPackage::getStatus, CommonConstants.STATUS_ENABLE)
                .like(StringUtil.isNotBlank(sysTenantPackage.getPackageName()), SysTenantPackage::getPackageName, sysTenantPackage.getPackageName());
        return ok(tenantPackageMapper.selectList(lqw));
    }

    @Operation(summary = "套餐菜单选择树")
    @GetMapping({"/menu_select_tree/", "/menu_select_tree/{packageId}"})
    @PreAuthorize("@ps.hasPerms('tenant_package:list')")
    public Result<?> tenantPackageMenuTreeList(@PathVariable(required = false) Long packageId) {
        Map<String, Object> res = new HashMap<>(2);
        res.put("checkedKeys", tenantPackageMapper.listMenuIdByPackageId(packageId));
        SysMenu sysMenu = new SysMenu(CommonConstants.STATUS_ENABLE);
        res.put("menus", menuService.listMenuSelectTree(sysMenu));// 查询租户专用菜单
        return ok(res);
    }

    @Operation(summary = "套餐详细信息")
    @GetMapping("/{packageId}")
    @PreAuthorize("@ps.hasPerms('tenant_package:query')")
    public Result<SysTenantPackage> getInfo(@NotNull(message = "[packageId] {validate.notnull}")
                                            @PathVariable Long packageId) {
        return ok(tenantPackageMapper.selectById(packageId));
    }

    @Operation(summary = "新增租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "新增套餐", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('tenant_package:add')")
    public Result<Void> add(@Valid @RequestBody SysTenantPackage tenantPackage) {
        return toRes(tenantPackageMapper.insert(tenantPackage));
    }

    @Operation(summary = "修改租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "修改套餐", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('tenant_package:edit')")
    public Result<Void> edit(@Valid @RequestBody SysTenantPackage tenantPackage) {
        return toRes(tenantPackageMapper.updateById(tenantPackage));
    }

    @Operation(summary = "删除租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "删除套餐", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('tenant_package:remove')")
    public Result<Void> remove(@NotEmpty(message = "[packageId] {validate.notnull}") @RequestBody List<Long> packageIds) {
        return toRes(tenantPackageMapper.deleteByIds(packageIds));
    }

}
