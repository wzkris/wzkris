package com.wzkris.auth.feign.token;

import com.wzkris.auth.feign.token.fallback.TokenHttpServiceFallbackFactory;
import com.wzkris.auth.feign.token.req.TokenReq;
import com.wzkris.auth.feign.token.resp.TokenResponse;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.core.model.domain.LoginClient;
import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.common.httpservice.annotation.HttpServiceClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - token服务 (HTTP Service Client 版本)
 * @date : 2025/01/24
 */
@HttpServiceClient(
        serviceId = ServiceIdConstant.AUTH,
        fallbackFactory = TokenHttpServiceFallbackFactory.class
)
@HttpExchange(url = "/feign-token")
public interface TokenHttpService {

    /**
     * 校验oauth2_token
     */
    @PostExchange("/check-client")
    TokenResponse<LoginClient> validateClient(@RequestBody TokenReq tokenReq);

    /**
     * 校验token
     */
    @PostExchange("/check-principal")
    TokenResponse<MyPrincipal> validatePrincipal(@RequestBody TokenReq tokenReq);

}

