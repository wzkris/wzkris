package com.wzkris.auth.rmi;

import com.wzkris.auth.rmi.domain.req.TokenReq;
import com.wzkris.auth.rmi.domain.resp.TokenResponse;
import com.wzkris.auth.rmi.fallback.RmiTokenFeignFallback;
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
@FeignClient(name = ServiceIdConstant.AUTH, contextId = "RmiTokenFeign", fallbackFactory = RmiTokenFeignFallback.class)
public interface RmiTokenFeign extends RmiFeign {

    String prefix = "/rmi_token";

    /**
     * 校验oauth2_token
     */
    @PostMapping(prefix + "/check_oauth2")
    TokenResponse checkOAuth2Token(@Valid @RequestBody TokenReq tokenReq);

    /**
     * 校验用户token
     */
    @PostMapping(prefix + "/check_user")
    TokenResponse checkUserToken(@Valid @RequestBody TokenReq tokenReq);

}
