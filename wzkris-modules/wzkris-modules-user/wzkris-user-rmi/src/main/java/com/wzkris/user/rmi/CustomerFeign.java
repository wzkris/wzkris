package com.wzkris.user.rmi;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.common.openfeign.core.RmiFeign;
import com.wzkris.user.rmi.domain.req.LoginInfoReq;
import com.wzkris.user.rmi.domain.resp.CustomerResp;
import com.wzkris.user.rmi.fallback.CustomerFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - APP用户
 * @date : 2024/4/15 16:20
 */
@FeignClient(name = ServiceIdConstant.USER, contextId = "CustomerFeign",
        fallbackFactory = CustomerFeignFallback.class,
        path = "/feign-customer")
public interface CustomerFeign extends RmiFeign {

    /**
     * 根据手机号查询app用户
     */
    @PostMapping("/query-by-phonenumber")
    CustomerResp getByPhoneNumber(@RequestBody String phoneNumber);

    /**
     * 根据authCode去不同渠道获取三方唯一标识，并转换成用户信息
     */
    @PostMapping("/query-or-register-by-identifier")
    CustomerResp getOrRegisterByIdentifier(@RequestParam String identifierType, @RequestParam String authCode);

    /**
     * 更新用户登录信息
     */
    @PostMapping("/update-logininfo")
    void updateLoginInfo(@RequestBody LoginInfoReq loginInfoReq);

}
