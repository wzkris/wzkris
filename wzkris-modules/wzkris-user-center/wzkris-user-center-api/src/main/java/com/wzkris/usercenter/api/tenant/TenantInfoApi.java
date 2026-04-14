package com.wzkris.usercenter.api.tenant;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.request.tenant.TenantInfoBasicUpdateRequest;
import com.wzkris.usercenter.response.tenant.TenantInfoQueryResponse;

public interface TenantInfoApi {

    Result<TenantInfoQueryResponse> queryInfo();

    Result<Void> updateBasicInfo(TenantInfoBasicUpdateRequest request);

    Result<Void> updateOperPwd(PasswordUpdateRequest request);

}
