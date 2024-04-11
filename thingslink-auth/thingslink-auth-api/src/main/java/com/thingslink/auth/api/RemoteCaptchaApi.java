package com.thingslink.auth.api;

import com.thingslink.auth.api.domain.SmsDTO;
import com.thingslink.auth.api.fallback.RemoteCaptchaApiFallback;
import com.thingslink.common.core.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 验证码服务
 * @date : 2023/8/21 11:27
 */
@FeignClient(value = "thingslink-auth", contextId = "RemoteCaptchaApi", fallbackFactory = RemoteCaptchaApiFallback.class)
public interface RemoteCaptchaApi {

    @PostMapping("/inner/sms/validate")
    Result<?> validateSms(@RequestBody SmsDTO smsDTO);
}
