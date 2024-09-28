package com.wzkris.auth.api;

import com.wzkris.auth.api.domain.SmsDTO;
import com.wzkris.auth.api.fallback.RemoteCaptchaApiFallback;
import com.wzkris.common.core.constant.ApplicationNameConstants;
import com.wzkris.common.core.domain.Result;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.wzkris.common.core.constant.SecurityConstants.INNER_REQUEST_PATH;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 验证码服务
 * @date : 2023/8/21 11:27
 */
@FeignClient(value = ApplicationNameConstants.AUTH, contextId = "RemoteCaptchaApi", fallbackFactory = RemoteCaptchaApiFallback.class)
public interface RemoteCaptchaApi {

    @PostMapping(INNER_REQUEST_PATH + "/sms/validate")
    Result<Void> validateSms(@RequestBody @Valid SmsDTO smsDTO);
}
