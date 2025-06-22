package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.oauth2.annotation.CheckSystemPerms;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.SysTenantPackage;
import com.wzkris.user.domain.req.EditStatusReq;
import com.wzkris.user.domain.req.SysTenantPackageQueryReq;
import com.wzkris.user.domain.req.SysTenantPackageReq;
import com.wzkris.user.domain.vo.CheckedSelectTreeVO;
import com.wzkris.user.mapper.SysTenantPackageMapper;
import com.wzkris.user.service.SysMenuService;
import com.wzkris.user.service.SysTenantPackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 租户套餐管理
 *
 * @author Michelle.Chung
 */
@Tag(name = "租户套餐管理")
@Validated
@RequiredArgsConstructor
@PreAuthorize("@su.isSuperTenant()") // 只允许超级租户访问
@RestController
@RequestMapping("/sys_tenant/package")
public class SysTenantPackageController extends BaseController {

    private final SysTenantPackageMapper tenantPackageMapper;

    private final SysTenantPackageService tenantPackageService;

    private final SysMenuService menuService;

    @Operation(summary = "套餐分页")
    @GetMapping("/list")
    @CheckSystemPerms("tenant_package:list")
    public Result<Page<SysTenantPackage>> listPage(SysTenantPackageQueryReq queryReq) {
        startPage();
        List<SysTenantPackage> list = tenantPackageMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<SysTenantPackage> buildQueryWrapper(SysTenantPackageQueryReq queryReq) {
        return new LambdaQueryWrapper<SysTenantPackage>()
                .select(SysTenantPackage.class, q -> !q.getColumn().equals("menu_ids"))
                .like(
                        StringUtil.isNotNull(queryReq.getPackageName()),
                        SysTenantPackage::getPackageName,
                        queryReq.getPackageName())
                .eq(StringUtil.isNotNull(queryReq.getStatus()), SysTenantPackage::getStatus, queryReq.getStatus())
                .orderByDesc(SysTenantPackage::getPackageId);
    }

    @Operation(summary = "套餐菜单选择树")
    @GetMapping({"/menu_checked_select_tree/", "/menu_checked_select_tree/{packageId}"})
    @CheckSystemPerms("tenant_package:query")
    public Result<CheckedSelectTreeVO> tenantPackageMenuTreeList(@PathVariable(required = false) Long packageId) {
        CheckedSelectTreeVO checkedSelectTreeVO = new CheckedSelectTreeVO();
        checkedSelectTreeVO.setCheckedKeys(tenantPackageMapper.listMenuIdByPackageId(packageId));
        checkedSelectTreeVO.setSelectTrees(menuService.listSelectTree(SystemUserUtil.getUserId()));
        return ok(checkedSelectTreeVO);
    }

    @Operation(summary = "套餐详细信息")
    @GetMapping("/{packageId}")
    @CheckSystemPerms("tenant_package:query")
    public Result<SysTenantPackage> getInfo(
            @NotNull(message = "{desc.package}{desc.id}{validate.notnull}") @PathVariable Long packageId) {
        return ok(tenantPackageMapper.selectById(packageId));
    }

    @Operation(summary = "新增租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "新增套餐", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckSystemPerms("tenant_package:add")
    public Result<Void> add(@Valid @RequestBody SysTenantPackageReq req) {
        return toRes(tenantPackageMapper.insert(BeanUtil.convert(req, SysTenantPackage.class)));
    }

    @Operation(summary = "修改租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "修改套餐", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckSystemPerms("tenant_package:edit")
    public Result<Void> edit(@Valid @RequestBody SysTenantPackageReq req) {
        return toRes(tenantPackageMapper.updateById(BeanUtil.convert(req, SysTenantPackage.class)));
    }

    @Operation(summary = "修改租户套餐状态")
    @OperateLog(title = "租户套餐", subTitle = "修改租户套餐状态", operateType = OperateType.UPDATE)
    @PostMapping("/edit_status")
    @CheckSystemPerms("tenant_package:edit")
    public Result<Void> editStatus(@RequestBody @Valid EditStatusReq statusReq) {
        SysTenantPackage update = new SysTenantPackage(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(tenantPackageMapper.updateById(update));
    }

    @Operation(summary = "删除租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "删除套餐", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckSystemPerms("tenant_package:remove")
    public Result<Void> remove(
            @NotEmpty(message = "{desc.package}{desc.id}{validate.notnull}") @RequestBody List<Long> packageIds) {
        if (tenantPackageService.checkPackageUsed(packageIds)) {
            return err412("删除失败, 套餐正在使用");
        }
        return toRes(tenantPackageMapper.deleteByIds(packageIds));
    }
}
