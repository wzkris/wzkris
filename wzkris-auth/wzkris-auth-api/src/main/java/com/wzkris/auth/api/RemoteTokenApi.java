package com.wzkris.auth.api;

import com.wzkris.auth.api.domain.request.TokenReq;
import com.wzkris.auth.api.domain.response.TokenResponse;
import jakarta.validation.Valid;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - token服务
 * @date : 2024/09/28 10:27
 */
public interface RemoteTokenApi {

    /**
     * 校验token
     */
    TokenResponse checkToken(@Valid TokenReq tokenReq);
}
