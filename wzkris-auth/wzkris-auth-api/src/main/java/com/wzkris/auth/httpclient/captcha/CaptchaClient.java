package com.wzkris.auth.httpclient.captcha;

import com.wzkris.auth.httpclient.captcha.req.CaptchaCheckReq;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.httpclient.annotation.HttpClient;
import com.wzkris.common.httpclient.constants.ServiceContextPathConstant;
import com.wzkris.common.httpclient.constants.ServiceIdConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 验证码服务
 * @date : 2023/8/21 11:27
 */
@HttpClient(
        serviceId = ServiceIdConstant.AUTH,
        path = ServiceContextPathConstant.AUTH
)
@HttpExchange(url = "/feign-captcha")
public interface CaptchaClient {

    /**
     * 校验手机号验证码
     */
    @PostExchange("/validate")
    Result<Boolean> validateCaptcha(@RequestBody CaptchaCheckReq captchaCheckReq);

}
