package com.wzkris.usercenter.api.tenantpackage;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.request.tenantpackage.TenantPackageMngPageRequest;
import com.wzkris.usercenter.request.tenantpackage.TenantPackageMngSaveRequest;
import com.wzkris.usercenter.request.tenantpackage.TenantPackageMngUpdateRequest;
import com.wzkris.usercenter.response.common.CheckedSelectTreeResponse;
import com.wzkris.usercenter.response.tenantpackage.TenantPackageMngResponse;

public interface TenantPackageMngApi {

    Result<Page<TenantPackageMngResponse>> queryPage(TenantPackageMngPageRequest request);

    Result<TenantPackageMngResponse> queryInfo(IdRequest request);

    Result<CheckedSelectTreeResponse> queryMenuSelectTree(IdRequest request);

    Result<Void> save(TenantPackageMngSaveRequest request);

    Result<Void> update(TenantPackageMngUpdateRequest request);

    Result<Void> remove(IdListRequest request);

}
