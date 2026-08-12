package com.wzkris.usercenter.remote.api.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.customer.request.CustomerQueryRequest;
import com.wzkris.usercenter.remote.api.customer.request.CustomerSocialUpdateRequest;
import com.wzkris.usercenter.remote.api.customer.request.SocialLoginRequest;
import com.wzkris.usercenter.remote.api.customer.response.CustomerQueryResponse;

import java.util.List;

public interface CustomerRemoteApi {

    Result<List<CustomerQueryResponse>> queryList(CustomerQueryRequest request);

    Result<CustomerQueryResponse> socialLogin(SocialLoginRequest request);

    Result<Void> updateSocialInfo(CustomerSocialUpdateRequest request);

    Result<Void> updateLoginInfo(LoginInfoUpdateRequest request);

}
