package com.wzkris.auth.httpclient.token;

import com.wzkris.auth.httpclient.token.fallback.LoginUserClientFallback;
import com.wzkris.auth.httpclient.token.req.LoginUserReq;
import com.wzkris.auth.httpclient.token.req.OAuth2TokenReq;
import com.wzkris.auth.httpclient.token.resp.LoginUserResp;
import com.wzkris.common.httpclient.annotation.HttpClient;
import com.wzkris.common.httpclient.constants.ServiceContextPathConstant;
import com.wzkris.common.httpclient.constants.ServiceIdConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - loginUser服务 (HTTP Service Client 版本)
 * @date : 2025/01/24
 */
@HttpClient(
        serviceId = ServiceIdConstant.AUTH,
        path = ServiceContextPathConstant.AUTH,
        fallbackFactory = LoginUserClientFallback.class
)
@HttpExchange(url = "/login-user-client")
public interface LoginUserClient {

    /**
     * 获取登录信息
     */
    @PostExchange("/query")
    LoginUserResp query(@RequestBody LoginUserReq loginUserReq);

    /**
     * 通过OAuth2 token获取用户信息
     */
    @PostExchange("/query-by-token")
    LoginUserResp queryByToken(@RequestBody OAuth2TokenReq request);

}

