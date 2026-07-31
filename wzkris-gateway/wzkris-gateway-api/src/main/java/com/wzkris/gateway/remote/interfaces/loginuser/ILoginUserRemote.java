package com.wzkris.gateway.remote.interfaces.loginuser;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.remote.annotation.RemoteInterface;
import com.wzkris.common.remote.constants.ServiceContextPathConstant;
import com.wzkris.common.remote.constants.ServiceIdConstant;
import com.wzkris.gateway.remote.interfaces.loginuser.request.LoginUserQueryRequest;
import com.wzkris.gateway.remote.interfaces.loginuser.request.OAuth2TokenQueryRequest;
import com.wzkris.gateway.remote.interfaces.loginuser.response.LoginUserResponse;
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
@RemoteInterface(
        serviceId = ServiceIdConstant.AUTH,
        path = ServiceContextPathConstant.AUTH
)
@HttpExchange(url = "/login-user-remote")
public interface ILoginUserRemote {

    /**
     * 获取登录信息
     */
    @PostExchange("/query-info")
    Result<LoginUserResponse> queryInfo(@Validated @RequestBody LoginUserQueryRequest LoginUserQueryRequest);

    /**
     * 通过OAuth2 token获取用户信息
     */
    @PostExchange("/query-oauth2")
    Result<LoginUserResponse> queryOAuth2(@Validated @RequestBody OAuth2TokenQueryRequest request);

}

