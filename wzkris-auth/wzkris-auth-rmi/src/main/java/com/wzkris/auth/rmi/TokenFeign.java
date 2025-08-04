package com.wzkris.auth.rmi;

import com.wzkris.auth.rmi.domain.req.TokenReq;
import com.wzkris.auth.rmi.domain.resp.TokenResponse;
import com.wzkris.auth.rmi.fallback.TokenFeignFallback;
import com.wzkris.common.openfeign.annotation.FeignIgnoreInterceptor;
import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.common.openfeign.core.RmiFeign;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - token服务
 * @date : 2024/09/28 10:27
 */
@FeignIgnoreInterceptor
@FeignClient(name = ServiceIdConstant.AUTH, contextId = "TokenFeign", fallbackFactory = TokenFeignFallback.class)
public interface TokenFeign extends RmiFeign {

    String prefix = "/rmi_token";

    /**
     * 校验oauth2_token
     */
    @PostMapping(prefix + "/check_oauth2")
    TokenResponse validateOAuth2(@Valid @RequestBody TokenReq tokenReq);

    /**
     * 校验用户token
     */
    @PostMapping(prefix + "/check_user")
    TokenResponse validateUser(@Valid @RequestBody TokenReq tokenReq);

}
