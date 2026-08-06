package com.wzkris.usercenter.api.tenant;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.api.tenant.request.TenantInfoBasicUpdateRequest;
import com.wzkris.usercenter.api.tenant.response.TenantInfoQueryResponse;
import com.wzkris.usercenter.request.PasswordUpdateRequest;

public interface TenantInfoApi {

    Result<TenantInfoQueryResponse> queryInfo();

    Result<Void> updateBasicInfo(TenantInfoBasicUpdateRequest request);

    Result<Void> updateOperPwd(PasswordUpdateRequest request);

}
