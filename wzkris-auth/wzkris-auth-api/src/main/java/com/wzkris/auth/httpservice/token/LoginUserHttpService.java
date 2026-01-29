package com.wzkris.auth.httpservice.token;

import com.wzkris.auth.httpservice.token.fallback.LoginUserHttpServiceFallback;
import com.wzkris.auth.httpservice.token.req.LoginUserReq;
import com.wzkris.auth.httpservice.token.resp.LoginUserResp;
import com.wzkris.common.httpservice.annotation.HttpServiceClient;
import com.wzkris.common.httpservice.constants.ServiceContextPathConstant;
import com.wzkris.common.httpservice.constants.ServiceIdConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - loginUser服务 (HTTP Service Client 版本)
 * @date : 2025/01/24
 */
@HttpServiceClient(
        serviceId = ServiceIdConstant.AUTH,
        path = ServiceContextPathConstant.AUTH,
        fallbackFactory = LoginUserHttpServiceFallback.class
)
@HttpExchange(url = "/http-login-user")
public interface LoginUserHttpService {

    /**
     * 获取登录信息
     */
    @PostExchange("/query")
    LoginUserResp query(@RequestBody LoginUserReq loginUserReq);

}

