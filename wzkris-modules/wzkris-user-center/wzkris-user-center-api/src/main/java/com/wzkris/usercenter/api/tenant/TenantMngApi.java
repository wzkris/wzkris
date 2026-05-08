package com.wzkris.usercenter.api.tenant;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.request.common.PwdResetRequest;
import com.wzkris.usercenter.request.tenant.TenantMngPageRequest;
import com.wzkris.usercenter.request.tenant.TenantMngSaveRequest;
import com.wzkris.usercenter.request.tenant.TenantMngUpdateRequest;
import com.wzkris.usercenter.request.tenantpackage.TenantPackageMngListRequest;
import com.wzkris.usercenter.response.common.SelectResponse;
import com.wzkris.usercenter.response.tenant.TenantMngResponse;

import java.util.List;

public interface TenantMngApi {

    Result<Page<TenantMngResponse>> queryPage(TenantMngPageRequest request);

    Result<TenantMngResponse> queryInfo(IdRequest request);

    Result<Page<SelectResponse>> querySelectPage(TenantMngPageRequest request);

    Result<List<SelectResponse>> queryPackageSelect(TenantPackageMngListRequest request);

    Result<Void> save(TenantMngSaveRequest tenantReq);

    Result<Void> update(TenantMngUpdateRequest tenantReq);

    Result<Void> resetOperPwd(PwdResetRequest request);

    Result<Void> remove(IdRequest request);

}
