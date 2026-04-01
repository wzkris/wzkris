package com.wzkris.usercenter.api.tenant;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.request.PwdResetRequest;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.tenant.TenantMngQueryRequest;
import com.wzkris.usercenter.request.tenant.TenantMngSaveRequest;
import com.wzkris.usercenter.request.tenant.TenantMngUpdateRequest;
import com.wzkris.usercenter.response.SelectResponse;
import com.wzkris.usercenter.response.tenant.TenantMngResponse;

import java.util.List;

public interface TenantMngApi {

    Result<Page<TenantMngResponse>> queryPage(TenantMngQueryRequest request);

    Result<TenantMngResponse> queryInfo(Long tenantId);

    Result<Page<SelectResponse>> querySelectPage(String tenantName);

    Result<List<SelectResponse>> queryPackageSelect(String packageName);

    Result<Void> save(TenantMngSaveRequest tenantReq);

    Result<Void> update(TenantMngUpdateRequest tenantReq);

    Result<Void> updateStatus(StatusUpdateRequest request);

    Result<Void> resetOperPwd(PwdResetRequest request);

    Result<Void> remove(Long tenantId);

}
