package com.wzkris.auth.rmi;

import com.wzkris.auth.rmi.domain.req.TokenReq;
import com.wzkris.auth.rmi.domain.resp.TokenResponse;
import jakarta.validation.Valid;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - token服务
 * @date : 2024/09/28 10:27
 */
public interface RmiTokenService {

    /**
     * 校验token
     */
    TokenResponse checkToken(@Valid TokenReq tokenReq);
}
