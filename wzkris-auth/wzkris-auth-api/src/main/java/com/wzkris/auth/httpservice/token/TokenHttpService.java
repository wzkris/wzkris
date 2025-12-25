package com.wzkris.auth.httpservice.token;

import com.wzkris.auth.httpservice.token.fallback.TokenHttpServiceFallback;
import com.wzkris.auth.httpservice.token.req.TokenReq;
import com.wzkris.auth.httpservice.token.resp.TokenResponse;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.httpservice.annotation.HttpServiceClient;
import com.wzkris.common.httpservice.constants.ServiceIdConstant;
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
        fallbackFactory = TokenHttpServiceFallback.class
)
@HttpExchange(url = "/feign-token")
public interface TokenHttpService {

    /**
     * 校验token
     */
    @PostExchange("/introspect")
    TokenResponse<MyPrincipal> introspect(@RequestBody TokenReq tokenReq);

}

