package com.wzkris.principal.feign.customer;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.principal.feign.admin.req.LoginInfoReq;
import com.wzkris.principal.feign.customer.fallback.CustomerInfoFeignFallback;
import com.wzkris.principal.feign.customer.req.WexcxLoginReq;
import com.wzkris.principal.feign.customer.resp.CustomerResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 客户
 * @date : 2024/4/15 16:20
 */
@FeignClient(name = ServiceIdConstant.PRINCIPAL, contextId = "CustomerFeign",
        fallbackFactory = CustomerInfoFeignFallback.class,
        path = "/feign-customer")
public interface CustomerInfoFeign {

    /**
     * 根据手机号查询客户
     */
    @PostMapping("/query-by-phonenumber")
    CustomerResp getByPhoneNumber(@RequestBody String phoneNumber);

    /**
     * 微信小程序获取信息或注册
     */
    @PostMapping("/wexcx-login")
    CustomerResp wexcxLogin(@RequestBody WexcxLoginReq req);

    /**
     * 更新用户登录信息
     */
    @PostMapping("/update-logininfo")
    void updateLoginInfo(@RequestBody LoginInfoReq loginInfoReq);

}
