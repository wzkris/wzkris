package com.wzkris.user.feign.customer;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.common.openfeign.core.RmiFeign;
import com.wzkris.user.feign.customer.fallback.CustomerFeignFallback;
import com.wzkris.user.feign.customer.req.SocialLoginReq;
import com.wzkris.user.feign.customer.resp.CustomerResp;
import com.wzkris.user.feign.userinfo.req.LoginInfoReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 客户
 * @date : 2024/4/15 16:20
 */
@FeignClient(name = ServiceIdConstant.USER, contextId = "CustomerFeign",
        fallbackFactory = CustomerFeignFallback.class,
        path = "/feign-customer")
public interface CustomerFeign extends RmiFeign {

    /**
     * 根据手机号查询客户
     */
    @PostMapping("/query-by-phonenumber")
    CustomerResp getByPhoneNumber(@RequestBody String phoneNumber);

    /**
     * 根据三方唯一标识获取信息或注册
     */
    @PostMapping("/social-login-query")
    CustomerResp socialLoginQuery(@RequestBody SocialLoginReq req);

    /**
     * 更新用户登录信息
     */
    @PostMapping("/update-logininfo")
    void updateLoginInfo(@RequestBody LoginInfoReq loginInfoReq);

}
