package com.wzkris.usercenter.api.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.customer.CustomerMngQueryRequest;
import com.wzkris.usercenter.response.customer.CustomerMngResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface CustomerMngApi {

    Result<Page<CustomerMngResponse>> queryPage(CustomerMngQueryRequest request);

    Result<CustomerMngResponse> queryInfo(Long customerId);

    Result<Void> updateStatus(StatusUpdateRequest request);

    void export(HttpServletResponse response, CustomerMngQueryRequest request);

}
