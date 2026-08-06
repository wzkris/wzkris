package com.wzkris.usercenter.api.tenant;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.tenant.request.TenantMngPageRequest;
import com.wzkris.usercenter.api.tenant.request.TenantMngSaveRequest;
import com.wzkris.usercenter.api.tenant.request.TenantMngUpdateRequest;
import com.wzkris.usercenter.api.tenant.response.TenantMngQueryResponse;
import com.wzkris.usercenter.api.tenant.response.TenantMngPageResponse;
import com.wzkris.usercenter.api.tenantpackage.request.TenantPackageMngListRequest;
import com.wzkris.usercenter.request.PwdResetRequest;
import com.wzkris.usercenter.response.SelectResponse;

import java.util.List;

public interface TenantMngApi {

    Result<Page<TenantMngPageResponse>> queryPage(TenantMngPageRequest request);

    Result<TenantMngQueryResponse> queryInfo(IdRequest request);

    Result<Page<SelectResponse>> querySelectPage(TenantMngPageRequest request);

    Result<List<SelectResponse>> queryPackageSelect(TenantPackageMngListRequest request);

    Result<Void> save(TenantMngSaveRequest tenantReq);

    Result<Void> update(TenantMngUpdateRequest tenantReq);

    Result<Void> resetOperPwd(PwdResetRequest request);

    Result<Void> remove(IdRequest request);

}
