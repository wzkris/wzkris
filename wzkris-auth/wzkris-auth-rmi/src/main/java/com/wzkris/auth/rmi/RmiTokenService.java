package com.wzkris.auth.rmi;

import com.wzkris.auth.rmi.domain.req.TokenReq;
import com.wzkris.auth.rmi.domain.resp.TokenResponse;
import com.wzkris.common.openfeign.constants.ApplicationConstant;
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
@FeignClient(name = ApplicationConstant.AUTH, contextId = "RmiTokenService")
public interface RmiTokenService {

    /**
     * 校验token
     */
    @PostMapping("/check_token")
    TokenResponse checkToken(@Valid @RequestBody TokenReq tokenReq);

}
