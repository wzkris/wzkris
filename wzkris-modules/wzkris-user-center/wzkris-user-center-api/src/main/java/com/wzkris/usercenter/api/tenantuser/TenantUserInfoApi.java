package com.wzkris.usercenter.api.tenantuser;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.api.tenantuser.request.TenantUserInfoBasicUpdateRequest;
import com.wzkris.usercenter.api.tenantuser.response.TenantUserInfoQueryResponse;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.request.PhoneNumberUpdateRequest;

public interface TenantUserInfoApi {

    Result<TenantUserInfoQueryResponse> query();

    Result<Void> updateBasicInfo(TenantUserInfoBasicUpdateRequest request);

    Result<Void> updatePhoneNumber(PhoneNumberUpdateRequest request);

    Result<Void> updatePwd(PasswordUpdateRequest request);

}
