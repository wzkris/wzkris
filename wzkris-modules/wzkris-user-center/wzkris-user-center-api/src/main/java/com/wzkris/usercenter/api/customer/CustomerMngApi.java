package com.wzkris.usercenter.api.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.request.customer.CustomerMngPageRequest;
import com.wzkris.usercenter.response.customer.CustomerMngResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface CustomerMngApi {

    Result<Page<CustomerMngResponse>> queryPage(CustomerMngPageRequest request);

    Result<CustomerMngResponse> queryInfo(IdRequest request);

    void export(HttpServletResponse response, CustomerMngPageRequest request);

}
