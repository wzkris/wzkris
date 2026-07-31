package com.wzkris.usercenter.remote.api.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.customer.request.CustomerQueryRequest;
import com.wzkris.usercenter.remote.api.customer.request.WexcxLoginRequest;
import com.wzkris.usercenter.remote.api.customer.response.CustomerResponse;
import com.wzkris.usercenter.request.StringValueRequest;

import java.util.List;

public interface CustomerInfoRemoteApi {

    Result<List<CustomerResponse>> queryList(CustomerQueryRequest request);

    Result<CustomerResponse> wexcxLogin(WexcxLoginRequest request);

    Result<Void> updateLoginInfo(LoginInfoUpdateRequest request);

}

