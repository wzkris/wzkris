package com.wzkris.usercenter.api.tenantrole;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.tenantrole.request.TenantRoleMngPageRequest;
import com.wzkris.usercenter.api.tenantrole.request.TenantRoleMngSaveRequest;
import com.wzkris.usercenter.api.tenantrole.request.TenantRoleMngUpdateRequest;
import com.wzkris.usercenter.api.tenantrole.response.TenantRoleMngQueryResponse;
import com.wzkris.usercenter.api.tenantrole.response.TenantRoleMngPageResponse;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;

public interface TenantRoleMngApi {

    Result<Page<TenantRoleMngPageResponse>> queryPage(TenantRoleMngPageRequest request);

    Result<TenantRoleMngQueryResponse> queryById(IdRequest request);

    Result<CheckedSelectTreeResponse> queryMenuSelectTree(IdRequest request);

    Result<Void> save(TenantRoleMngSaveRequest request);

    Result<Void> update(TenantRoleMngUpdateRequest request);

    Result<Void> remove(IdListRequest request);

}
