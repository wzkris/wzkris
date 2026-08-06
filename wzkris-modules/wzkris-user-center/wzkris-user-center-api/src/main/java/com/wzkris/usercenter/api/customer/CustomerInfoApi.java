package com.wzkris.usercenter.api.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.api.customer.request.CustomerInfoBasicUpdateRequest;
import com.wzkris.usercenter.api.customer.response.CustomerInfoQueryResponse;

public interface CustomerInfoApi {

    Result<CustomerInfoQueryResponse> queryInfo();

    Result<?> updateBasicInfo(CustomerInfoBasicUpdateRequest request);

}
