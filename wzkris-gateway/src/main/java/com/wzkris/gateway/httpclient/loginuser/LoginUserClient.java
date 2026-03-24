package com.wzkris.gateway.httpclient.loginuser;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.httpclient.annotation.HttpClient;
import com.wzkris.common.httpclient.constants.ServiceContextPathConstant;
import com.wzkris.common.httpclient.constants.ServiceIdConstant;
import com.wzkris.gateway.httpclient.loginuser.req.LoginUserQueryReq;
import com.wzkris.gateway.httpclient.loginuser.req.OAuth2TokenQueryReq;
import com.wzkris.gateway.httpclient.loginuser.resp.LoginUserResp;
import org.springframework.validation.annotation.Validated;
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
        path = ServiceContextPathConstant.AUTH
)
@HttpExchange(url = "/login-user-client")
public interface LoginUserClient {

    /**
     * 获取登录信息
     */
    @PostExchange("/query-info")
    Result<LoginUserResp> queryInfo(@Validated @RequestBody LoginUserQueryReq loginUserQueryReq);

    /**
     * 通过OAuth2 token获取用户信息
     */
    @PostExchange("/query-oauth2")
    Result<LoginUserResp> queryOAuth2(@Validated @RequestBody OAuth2TokenQueryReq request);

}
