package com.wzkris.auth.api;

import com.wzkris.auth.api.domain.request.TokenReq;
import com.wzkris.auth.api.fallback.RemoteTokenApiFallback;
import com.wzkris.common.core.constant.ApplicationNameConstants;
import com.wzkris.common.core.domain.Result;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static com.wzkris.common.core.constant.SecurityConstants.INNER_NOAUTH_REQUEST_PATH;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - token服务
 * @date : 2024/09/28 10:27
 */
@FeignClient(value = ApplicationNameConstants.AUTH, contextId = "RemoteTokenApi", fallbackFactory = RemoteTokenApiFallback.class)
public interface RemoteTokenApi {

    /**
     * 获取token对应的请求id
     */
    @GetMapping(INNER_NOAUTH_REQUEST_PATH + "/oauth2/token/req_id")
    Result<String> getTokenReqId(@RequestParam String token);

    /**
     * 校验token
     */
    @PostMapping(INNER_NOAUTH_REQUEST_PATH + "/oauth2/check_token")
    Result<?> checkToken(@RequestBody @Valid TokenReq tokenReq);
}
