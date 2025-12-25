package com.wzkris.auth.httpservice.captcha;

import com.wzkris.auth.httpservice.captcha.fallback.CaptchaHttpServiceFallback;
import com.wzkris.auth.httpservice.captcha.req.CaptchaCheckReq;
import com.wzkris.common.httpservice.annotation.HttpServiceClient;
import com.wzkris.common.httpservice.constants.ServiceIdConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 验证码服务
 * @date : 2023/8/21 11:27
 */
@HttpServiceClient(
        serviceId = ServiceIdConstant.AUTH,
        fallbackFactory = CaptchaHttpServiceFallback.class
)
@HttpExchange(url = "/feign-captcha")
public interface CaptchaHttpService {

    /**
     * 校验手机号验证码
     */
    @PostExchange("/validate")
    boolean validateCaptcha(@RequestBody CaptchaCheckReq captchaCheckReq);

}
