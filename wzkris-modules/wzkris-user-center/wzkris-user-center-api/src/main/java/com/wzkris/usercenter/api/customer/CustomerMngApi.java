package com.wzkris.usercenter.api.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.customer.request.CustomerMngPageRequest;
import com.wzkris.usercenter.api.customer.response.CustomerMngQueryResponse;
import com.wzkris.usercenter.api.customer.response.CustomerMngPageResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface CustomerMngApi {

    Result<Page<CustomerMngPageResponse>> queryPage(CustomerMngPageRequest request);

    Result<CustomerMngQueryResponse> queryById(IdRequest request);

    void export(HttpServletResponse response, CustomerMngPageRequest request);

}
