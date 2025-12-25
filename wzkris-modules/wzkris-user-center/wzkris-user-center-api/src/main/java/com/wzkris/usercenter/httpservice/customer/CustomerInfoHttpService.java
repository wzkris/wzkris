package com.wzkris.usercenter.httpservice.customer;

import com.wzkris.common.httpservice.annotation.HttpServiceClient;
import com.wzkris.common.httpservice.constants.ServiceIdConstant;
import com.wzkris.usercenter.httpservice.admin.req.LoginInfoReq;
import com.wzkris.usercenter.httpservice.customer.fallback.CustomerInfoHttpServiceFallback;
import com.wzkris.usercenter.httpservice.customer.req.WexcxLoginReq;
import com.wzkris.usercenter.httpservice.customer.resp.CustomerResp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 客户
 * @date : 2024/4/15 16:20
 */
@HttpServiceClient(
        serviceId = ServiceIdConstant.USER_CENTER,
        fallbackFactory = CustomerInfoHttpServiceFallback.class
)
@HttpExchange(url = "/feign-customer")
public interface CustomerInfoHttpService {

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
