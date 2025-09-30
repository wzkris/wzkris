package com.wzkris.auth.feign.token;

import com.wzkris.auth.feign.token.fallback.TokenFeignFallback;
import com.wzkris.auth.feign.token.req.TokenReq;
import com.wzkris.auth.feign.token.resp.TokenResponse;
import com.wzkris.common.core.model.CorePrincipal;
import com.wzkris.common.core.model.domain.LoginClient;
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
@FeignClient(name = ServiceIdConstant.AUTH, contextId = "TokenFeign",
        fallbackFactory = TokenFeignFallback.class,
        path = "/feign-token")
public interface TokenFeign extends RmiFeign {

    /**
     * 校验oauth2_token
     */
    @PostMapping("/check-client")
    TokenResponse<LoginClient> validateClient(@Valid @RequestBody TokenReq tokenReq);

    /**
     * 校验token
     */
    @PostMapping("/check-principal")
    TokenResponse<CorePrincipal> validatePrincipal(@Valid @RequestBody TokenReq tokenReq);

}
