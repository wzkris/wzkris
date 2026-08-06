package com.wzkris.usercenter.api.customerlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.customerlog.login.request.CustomerLoginLogInfoPageRequest;
import com.wzkris.usercenter.api.customerlog.login.response.CustomerLoginLogInfoPageResponse;

public interface CustomerLoginlogInfoApi {

    Result<Page<CustomerLoginLogInfoPageResponse>> queryPage(CustomerLoginLogInfoPageRequest request);

}
