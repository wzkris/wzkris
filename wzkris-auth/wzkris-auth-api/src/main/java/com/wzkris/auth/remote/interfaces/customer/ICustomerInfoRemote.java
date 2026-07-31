package com.wzkris.auth.remote.interfaces.customer;

import com.wzkris.auth.remote.interfaces.admin.request.LoginInfoUpdateRequest;
import com.wzkris.auth.remote.interfaces.customer.request.CustomerQueryRequest;
import com.wzkris.auth.remote.interfaces.customer.request.WexcxLoginRequest;
import com.wzkris.auth.remote.interfaces.customer.response.CustomerResponse;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.remote.annotation.RemoteInterface;
import com.wzkris.common.remote.constants.ServiceContextPathConstant;
import com.wzkris.common.remote.constants.ServiceIdConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 客户
 * @date : 2024/4/15 16:20
 */
@RemoteInterface(
        serviceId = ServiceIdConstant.USER_CENTER,
        path = ServiceContextPathConstant.USER_CENTER
)
@HttpExchange(url = "/customer-info-remote")
public interface ICustomerInfoRemote {

    @PostExchange("/query-list")
    Result<List<CustomerResponse>> queryList(@RequestBody CustomerQueryRequest request);

    /**
     * 微信小程序获取信息或注册
     */
    @PostExchange("/wexcx-login")
    Result<CustomerResponse> wexcxLogin(@RequestBody WexcxLoginRequest request);

    /**
     * 更新用户登录信息
     */
    @PostExchange("/update-logininfo")
    Result<Void> updateLoginInfo(@RequestBody LoginInfoUpdateRequest LoginInfoUpdateRequest);

}

