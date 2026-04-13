package com.wzkris.usercenter.impl.tenantpackage;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.tenantpackage.TenantPackageMngApi;
import com.wzkris.usercenter.domain.TenantPackageInfoDO;
import com.wzkris.usercenter.mapper.TenantPackageInfoMapper;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.tenantpackage.TenantPackageMngQueryRequest;
import com.wzkris.usercenter.request.tenantpackage.TenantPackageMngSaveRequest;
import com.wzkris.usercenter.request.tenantpackage.TenantPackageMngUpdateRequest;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;
import com.wzkris.usercenter.response.tenantpackage.TenantPackageInfoResponse;
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
    public Result<Page<TenantPackageInfoResponse>> queryPage(TenantPackageMngQueryRequest request) {
        startPage();
        List<TenantPackageInfoDO> list = tenantPackageInfoService.list(this.buildQueryWrapper(request));
        return getPageResult(BeanUtil.convert(list, TenantPackageInfoResponse.class));
    }

    private LambdaQueryWrapper<TenantPackageInfoDO> buildQueryWrapper(TenantPackageMngQueryRequest request) {
        return new LambdaQueryWrapper<TenantPackageInfoDO>()
                .select(TenantPackageInfoDO.class, q -> !q.getColumn().equals("menu_ids"))
                .like(StringUtil.isNotEmpty(request.getPackageName()),
                        TenantPackageInfoDO::getPackageName,
                        request.getPackageName())
                .eq(StringUtil.isNotEmpty(request.getStatus()), TenantPackageInfoDO::getStatus, request.getStatus())
                .orderByDesc(TenantPackageInfoDO::getPackageId);
    }

    @Override
    public Result<TenantPackageInfoResponse> queryInfo(Long packageId) {
        return ok(BeanUtil.convert(tenantPackageInfoService.getById(packageId), TenantPackageInfoResponse.class));
    }

    @Override
    public Result<CheckedSelectTreeResponse> queryMenuSelectTree(Long packageId) {
        CheckedSelectTreeResponse checkedSelectTreeResponse = new CheckedSelectTreeResponse();
        checkedSelectTreeResponse.setCheckedKeys(tenantPackageInfoMapper.listMenuIdByPackageId(packageId));
        checkedSelectTreeResponse.setSelectTrees(menuInfoService.listAllTenantSelectTree());
        return ok(checkedSelectTreeResponse);
    }

    @Override
    public Result<Void> save(TenantPackageMngSaveRequest request) {
        return toRes(tenantPackageInfoService.save(BeanUtil.convert(request, TenantPackageInfoDO.class)));
    }

    @Override
    public Result<Void> update(TenantPackageMngUpdateRequest request) {
        return toRes(tenantPackageInfoService.updateById(BeanUtil.convert(request, TenantPackageInfoDO.class)));
    }

    @Override
    public Result<Void> updateStatus(StatusUpdateRequest request) {
        TenantPackageInfoDO update = new TenantPackageInfoDO(request.getId());
        update.setStatus(request.getStatus());
        return toRes(tenantPackageInfoService.updateById(update));
    }

    @Override
    public Result<Void> remove(List<Long> packageIds) {
        if (tenantPackageInfoService.existInUsed(packageIds)) {
            return requestFail("删除失败, 套餐正在使用");
        }
        return toRes(tenantPackageInfoService.removeByIds(packageIds));
    }

}
