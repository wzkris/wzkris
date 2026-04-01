package com.wzkris.usercenter.api.tenant;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.request.tenant.TenantInfoBasicUpdateRequest;
import com.wzkris.usercenter.response.tenant.TenantInfoResponse;
import com.wzkris.usercenter.response.tenant.TenantUsedQuotaResponse;

public interface TenantInfoApi {

    Result<TenantInfoResponse> queryInfo();

    Result<Void> updateBasicInfo(TenantInfoBasicUpdateRequest request);

    Result<TenantUsedQuotaResponse> queryLimitInfo();

    Result<Void> updateOperPwd(PasswordUpdateRequest request);

}
