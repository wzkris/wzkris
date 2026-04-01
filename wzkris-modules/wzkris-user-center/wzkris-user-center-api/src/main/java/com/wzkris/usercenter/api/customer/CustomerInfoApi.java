package com.wzkris.usercenter.api.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.request.customer.CustomerInfoBasicUpdateRequest;
import com.wzkris.usercenter.response.customer.CustomerInfoResponse;

public interface CustomerInfoApi {

    Result<CustomerInfoResponse> queryInfo();

    Result<?> updateBasicInfo(CustomerInfoBasicUpdateRequest request);

}
