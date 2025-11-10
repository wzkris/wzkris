package com.wzkris.principal.controller.tenantpackage;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.common.security.annotation.enums.CheckMode;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.principal.domain.TenantPackageInfoDO;
import com.wzkris.principal.domain.req.EditStatusReq;
import com.wzkris.principal.domain.req.tenantpackage.TenantPackageMngQueryReq;
import com.wzkris.principal.domain.req.tenantpackage.TenantPackageMngReq;
import com.wzkris.principal.domain.vo.CheckedSelectTreeVO;
import com.wzkris.principal.mapper.TenantPackageInfoMapper;
import com.wzkris.principal.service.MenuInfoService;
import com.wzkris.principal.service.TenantPackageInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户套餐管理
 *
 * @author wzkris
 */
@Tag(name = "租户套餐管理")
@Validated
@RestController
@RequestMapping("/tenant-package-manage")
@RequiredArgsConstructor
public class TenantPackageMngController extends BaseController {

    private final TenantPackageInfoMapper tenantPackageInfoMapper;

    private final TenantPackageInfoService tenantPackageInfoService;

    private final MenuInfoService menuInfoService;

    @Operation(summary = "套餐分页")
    @GetMapping("/list")
    @CheckAdminPerms("prin-mod:tenantpackage-mng:list")
    public Result<Page<TenantPackageInfoDO>> listPage(TenantPackageMngQueryReq queryReq) {
        startPage();
        List<TenantPackageInfoDO> list = tenantPackageInfoMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<TenantPackageInfoDO> buildQueryWrapper(TenantPackageMngQueryReq queryReq) {
        return new LambdaQueryWrapper<TenantPackageInfoDO>()
                .select(TenantPackageInfoDO.class, q -> !q.getColumn().equals("menu_ids"))
                .like(StringUtil.isNotEmpty(queryReq.getPackageName()),
                        TenantPackageInfoDO::getPackageName,
                        queryReq.getPackageName())
                .eq(StringUtil.isNotEmpty(queryReq.getStatus()), TenantPackageInfoDO::getStatus, queryReq.getStatus())
                .orderByDesc(TenantPackageInfoDO::getPackageId);
    }

    @Operation(summary = "套餐详细信息")
    @GetMapping("/{packageId}")
    @CheckAdminPerms("prin-mod:tenantpackage-mng:list")
    public Result<TenantPackageInfoDO> getInfo(
            @NotNull(message = "{invalidParameter.id.invalid}") @PathVariable Long packageId) {
        return ok(tenantPackageInfoMapper.selectById(packageId));
    }

    @Operation(summary = "套餐菜单选择树")
    @GetMapping({"/menu-checked-selecttree/", "/menu-checked-selecttree/{packageId}"})
    @CheckAdminPerms(
            value = {"prin-mod:tenantpackage-mng:add", "prin-mod:tenantpackage-mng:edit"},
            mode = CheckMode.OR)
    public Result<CheckedSelectTreeVO> tenantPackageMenuTreeList(@PathVariable(required = false) Long packageId) {
        CheckedSelectTreeVO checkedSelectTreeVO = new CheckedSelectTreeVO();
        checkedSelectTreeVO.setCheckedKeys(tenantPackageInfoMapper.listMenuIdByPackageId(packageId));
        checkedSelectTreeVO.setSelectTrees(menuInfoService.listAllTenantSelectTree());
        return ok(checkedSelectTreeVO);
    }

    @Operation(summary = "新增租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "新增套餐", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckAdminPerms("prin-mod:tenantpackage-mng:add")
    public Result<Void> add(@Valid @RequestBody TenantPackageMngReq req) {
        return toRes(tenantPackageInfoMapper.insert(BeanUtil.convert(req, TenantPackageInfoDO.class)));
    }

    @Operation(summary = "修改租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "修改套餐", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckAdminPerms("prin-mod:tenantpackage-mng:edit")
    public Result<Void> edit(@Valid @RequestBody TenantPackageMngReq req) {
        return toRes(tenantPackageInfoMapper.updateById(BeanUtil.convert(req, TenantPackageInfoDO.class)));
    }

    @Operation(summary = "修改租户套餐状态")
    @OperateLog(title = "租户套餐", subTitle = "修改租户套餐状态", operateType = OperateType.UPDATE)
    @PostMapping("/edit-status")
    @CheckAdminPerms("prin-mod:tenantpackage-mng:edit")
    public Result<Void> editStatus(@RequestBody @Valid EditStatusReq statusReq) {
        TenantPackageInfoDO update = new TenantPackageInfoDO(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(tenantPackageInfoMapper.updateById(update));
    }

    @Operation(summary = "删除租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "删除套餐", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("prin-mod:tenantpackage-mng:remove")
    public Result<Void> remove(
            @NotEmpty(message = "{invalidParameter.id.invalid}") @RequestBody List<Long> packageIds) {
        if (tenantPackageInfoService.checkPackageUsed(packageIds)) {
            return err40000("删除失败, 套餐正在使用");
        }
        return toRes(tenantPackageInfoMapper.deleteByIds(packageIds));
    }

}
