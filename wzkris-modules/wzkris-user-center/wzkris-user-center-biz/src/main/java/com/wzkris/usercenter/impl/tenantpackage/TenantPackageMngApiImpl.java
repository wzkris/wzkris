package com.wzkris.usercenter.impl.tenantpackage;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.tenantpackage.TenantPackageMngApi;
import com.wzkris.usercenter.api.tenantpackage.request.TenantPackageMngPageRequest;
import com.wzkris.usercenter.api.tenantpackage.request.TenantPackageMngSaveRequest;
import com.wzkris.usercenter.api.tenantpackage.request.TenantPackageMngUpdateRequest;
import com.wzkris.usercenter.api.tenantpackage.response.TenantPackageMngResponse;
import com.wzkris.usercenter.domain.TenantPackageInfoDO;
import com.wzkris.usercenter.mapper.TenantPackageInfoMapper;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;
import com.wzkris.usercenter.service.MenuInfoService;
import com.wzkris.usercenter.service.TenantPackageInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantPackageMngApiImpl extends AbstractApi implements TenantPackageMngApi {

    private final TenantPackageInfoMapper tenantPackageInfoMapper;

    private final TenantPackageInfoService tenantPackageInfoService;

    private final MenuInfoService menuInfoService;

    @Override
    public Result<Page<TenantPackageMngResponse>> queryPage(TenantPackageMngPageRequest request) {
        IPage<TenantPackageInfoDO> page = tenantPackageInfoService.page(request.buildPage(), this.buildQueryWrapper(request));
        return ok(Page.of(page, BeanUtil.convert(page.getRecords(), TenantPackageMngResponse.class)));
    }

    private LambdaQueryWrapper<TenantPackageInfoDO> buildQueryWrapper(TenantPackageMngPageRequest request) {
        return new LambdaQueryWrapper<TenantPackageInfoDO>()
                .select(TenantPackageInfoDO.class, q -> !q.getColumn().equals("menu_ids"))
                .like(StringUtil.isNotEmpty(request.getPackageName()),
                        TenantPackageInfoDO::getPackageName,
                        request.getPackageName())
                .eq(request.getStatus() != null, TenantPackageInfoDO::getStatus, request.getStatus())
                .orderByDesc(TenantPackageInfoDO::getPackageId);
    }

    @Override
    public Result<TenantPackageMngResponse> queryInfo(IdRequest request) {
        return ok(BeanUtil.convert(tenantPackageInfoService.getById(request.getId()), TenantPackageMngResponse.class));
    }

    @Override
    public Result<CheckedSelectTreeResponse> queryMenuSelectTree(IdRequest request) {
        Long packageId = request.getId();
        CheckedSelectTreeResponse checkedSelectTreeResponse = new CheckedSelectTreeResponse();
        checkedSelectTreeResponse.setCheckedKeys(tenantPackageInfoMapper.listMenuIdByPackageId(packageId));
        checkedSelectTreeResponse.setSelectTrees(menuInfoService.listAllTenantSelectTree());
        return ok(checkedSelectTreeResponse);
    }

    @Override
    public Result<Void> save(TenantPackageMngSaveRequest request) {
        TenantPackageInfoDO tenantPackageInfoDO = BeanUtil.convert(request, TenantPackageInfoDO.class);
        tenantPackageInfoDO.setStatus(request.getStatus());
        return toRes(tenantPackageInfoService.save(tenantPackageInfoDO));
    }

    @Override
    public Result<Void> update(TenantPackageMngUpdateRequest request) {
        TenantPackageInfoDO tenantPackageInfoDO = BeanUtil.convert(request, TenantPackageInfoDO.class);
        tenantPackageInfoDO.setStatus(request.getStatus());
        return toRes(tenantPackageInfoService.updateById(tenantPackageInfoDO));
    }

    @Override
    public Result<Void> remove(IdListRequest request) {
        List<Long> packageIds = request.getIdList();
        if (tenantPackageInfoService.existInUsed(packageIds)) {
            return requestFail("删除失败, 套餐正在使用");
        }
        return toRes(tenantPackageInfoService.removeByIds(packageIds));
    }

}
