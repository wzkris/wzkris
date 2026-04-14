package com.wzkris.usercenter.api.tenantpackage;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.tenantpackage.TenantPackageMngQueryRequest;
import com.wzkris.usercenter.request.tenantpackage.TenantPackageMngSaveRequest;
import com.wzkris.usercenter.request.tenantpackage.TenantPackageMngUpdateRequest;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;
import com.wzkris.usercenter.response.tenantpackage.TenantPackageMngQueryResponse;

import java.util.List;

public interface TenantPackageMngApi {

    Result<Page<TenantPackageMngQueryResponse>> queryPage(TenantPackageMngQueryRequest request);

    Result<TenantPackageMngQueryResponse> queryInfo(Long packageId);

    Result<CheckedSelectTreeResponse> queryMenuSelectTree(Long packageId);

    Result<Void> save(TenantPackageMngSaveRequest request);

    Result<Void> update(TenantPackageMngUpdateRequest request);

    Result<Void> updateStatus(StatusUpdateRequest request);

    Result<Void> remove(List<Long> packageIds);

}
