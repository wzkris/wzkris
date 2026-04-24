package com.wzkris.usercenter.remote.api.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.customer.request.WexcxLoginRequest;
import com.wzkris.usercenter.remote.api.customer.response.CustomerResponse;
import com.wzkris.usercenter.request.common.StringValueRequest;

public interface CustomerInfoRemoteApi {

    Result<CustomerResponse> queryByPhoneNumber(StringValueRequest request);

    Result<CustomerResponse> wexcxLogin(WexcxLoginRequest request);

    Result<Void> updateLoginInfo(LoginInfoUpdateRequest request);

}

