package com.wzkris.usercenter.httpclient.customer;

import com.wzkris.common.httpclient.annotation.HttpClient;
import com.wzkris.common.httpclient.constants.ServiceContextPathConstant;
import com.wzkris.common.httpclient.constants.ServiceIdConstant;
import com.wzkris.usercenter.httpclient.admin.req.LoginInfoReq;
import com.wzkris.usercenter.httpclient.customer.fallback.CustomerInfoClientFallback;
import com.wzkris.usercenter.httpclient.customer.req.WexcxLoginReq;
import com.wzkris.usercenter.httpclient.customer.resp.CustomerResp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 客户
 * @date : 2024/4/15 16:20
 */
@HttpClient(
        serviceId = ServiceIdConstant.USER_CENTER,
        path = ServiceContextPathConstant.USER_CENTER,
        fallbackFactory = CustomerInfoClientFallback.class
)
@HttpExchange(url = "/customer-info-client")
public interface CustomerInfoClient {

    /**
     * 根据手机号查询客户
     */
    @PostExchange("/query-by-phonenumber")
    CustomerResp getByPhoneNumber(@RequestBody String phoneNumber);

    /**
     * 微信小程序获取信息或注册
     */
    @PostExchange("/wexcx-login")
    CustomerResp wexcxLogin(@RequestBody WexcxLoginReq req);

    /**
     * 更新用户登录信息
     */
    @PostExchange("/update-logininfo")
    void updateLoginInfo(@RequestBody LoginInfoReq loginInfoReq);

}
