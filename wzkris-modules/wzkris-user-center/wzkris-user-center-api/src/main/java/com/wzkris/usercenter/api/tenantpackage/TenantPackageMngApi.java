package com.wzkris.usercenter.api.tenantpackage;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.tenantpackage.request.TenantPackageMngPageRequest;
import com.wzkris.usercenter.api.tenantpackage.request.TenantPackageMngSaveRequest;
import com.wzkris.usercenter.api.tenantpackage.request.TenantPackageMngUpdateRequest;
import com.wzkris.usercenter.api.tenantpackage.response.TenantPackageMngResponse;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;

public interface TenantPackageMngApi {

    Result<Page<TenantPackageMngResponse>> queryPage(TenantPackageMngPageRequest request);

    Result<TenantPackageMngResponse> queryInfo(IdRequest request);

    Result<CheckedSelectTreeResponse> queryMenuSelectTree(IdRequest request);

    Result<Void> save(TenantPackageMngSaveRequest request);

    Result<Void> update(TenantPackageMngUpdateRequest request);

    Result<Void> remove(IdListRequest request);

}
