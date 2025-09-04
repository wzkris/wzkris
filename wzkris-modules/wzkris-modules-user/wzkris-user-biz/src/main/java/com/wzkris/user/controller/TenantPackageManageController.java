package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckSystemPerms;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.user.domain.TenantPackageInfoDO;
import com.wzkris.user.domain.req.EditStatusReq;
import com.wzkris.user.domain.req.TenantPackageInfoReq;
import com.wzkris.user.domain.req.TenantPackageManageQueryReq;
import com.wzkris.user.domain.vo.CheckedSelectTreeVO;
import com.wzkris.user.mapper.TenantPackageInfoMapper;
import com.wzkris.user.service.MenuInfoService;
import com.wzkris.user.service.TenantPackageInfoService;
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
@PreAuthorize("@su.isSuperTenant()") // 只允许超级租户访问
@RestController
@RequestMapping("/tenant-package-manage")
public class TenantPackageManageController extends BaseController {

    private final TenantPackageInfoMapper tenantPackageInfoMapper;

    private final TenantPackageInfoService tenantPackageInfoService;

    private final MenuInfoService menuInfoService;

    @Operation(summary = "套餐分页")
    @GetMapping("/list")
    @CheckSystemPerms("user-mod:tenantpackage-mng:list")
    public Result<Page<TenantPackageInfoDO>> listPage(TenantPackageManageQueryReq queryReq) {
        startPage();
        List<TenantPackageInfoDO> list = tenantPackageInfoMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<TenantPackageInfoDO> buildQueryWrapper(TenantPackageManageQueryReq queryReq) {
        return new LambdaQueryWrapper<TenantPackageInfoDO>()
                .select(TenantPackageInfoDO.class, q -> !q.getColumn().equals("menu_ids"))
                .like(
                        StringUtil.isNotEmpty(queryReq.getPackageName()),
                        TenantPackageInfoDO::getPackageName,
                        queryReq.getPackageName())
                .eq(StringUtil.isNotEmpty(queryReq.getStatus()), TenantPackageInfoDO::getStatus, queryReq.getStatus())
                .orderByDesc(TenantPackageInfoDO::getPackageId);
    }

    @Operation(summary = "套餐菜单选择树")
    @GetMapping({"/menu-checked-selecttree/", "/menu-checked-selecttree/{packageId}"})
    @CheckSystemPerms("user-mod:tenantpackage-mng:query")
    public Result<CheckedSelectTreeVO> tenantPackageMenuTreeList(@PathVariable(required = false) Long packageId) {
        CheckedSelectTreeVO checkedSelectTreeVO = new CheckedSelectTreeVO();
        checkedSelectTreeVO.setCheckedKeys(tenantPackageInfoMapper.listMenuIdByPackageId(packageId));
        checkedSelectTreeVO.setSelectTrees(menuInfoService.listSelectTree(LoginUserUtil.getId()));
        return ok(checkedSelectTreeVO);
    }

    @Operation(summary = "套餐详细信息")
    @GetMapping("/{packageId}")
    @CheckSystemPerms("user-mod:tenantpackage-mng:query")
    public Result<TenantPackageInfoDO> getInfo(
            @NotNull(message = "{invalidParameter.id.invalid}") @PathVariable Long packageId) {
        return ok(tenantPackageInfoMapper.selectById(packageId));
    }

    @Operation(summary = "新增租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "新增套餐", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckSystemPerms("user-mod:tenantpackage-mng:add")
    public Result<Void> add(@Valid @RequestBody TenantPackageInfoReq req) {
        return toRes(tenantPackageInfoMapper.insert(BeanUtil.convert(req, TenantPackageInfoDO.class)));
    }

    @Operation(summary = "修改租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "修改套餐", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckSystemPerms("user-mod:tenantpackage-mng:edit")
    public Result<Void> edit(@Valid @RequestBody TenantPackageInfoReq req) {
        return toRes(tenantPackageInfoMapper.updateById(BeanUtil.convert(req, TenantPackageInfoDO.class)));
    }

    @Operation(summary = "修改租户套餐状态")
    @OperateLog(title = "租户套餐", subTitle = "修改租户套餐状态", operateType = OperateType.UPDATE)
    @PostMapping("/edit-status")
    @CheckSystemPerms("user-mod:tenantpackage-mng:edit")
    public Result<Void> editStatus(@RequestBody @Valid EditStatusReq statusReq) {
        TenantPackageInfoDO update = new TenantPackageInfoDO(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(tenantPackageInfoMapper.updateById(update));
    }

    @Operation(summary = "删除租户套餐")
    @OperateLog(title = "租户套餐", subTitle = "删除套餐", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckSystemPerms("user-mod:tenantpackage-mng:remove")
    public Result<Void> remove(
            @NotEmpty(message = "{invalidParameter.id.invalid}") @RequestBody List<Long> packageIds) {
        if (tenantPackageInfoService.checkPackageUsed(packageIds)) {
            return err40000("删除失败, 套餐正在使用");
        }
        return toRes(tenantPackageInfoMapper.deleteByIds(packageIds));
    }

}
