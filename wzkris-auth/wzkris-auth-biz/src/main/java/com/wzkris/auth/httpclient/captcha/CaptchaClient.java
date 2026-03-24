package com.wzkris.auth.httpclient.captcha;

import com.wzkris.auth.httpclient.captcha.req.CaptchaCheckReq;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.httpclient.annotation.HttpClient;
import com.wzkris.common.httpclient.constants.ServiceContextPathConstant;
import com.wzkris.common.httpclient.constants.ServiceIdConstant;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.service.annotation.HttpExchange;

@HttpClient(
        serviceId = ServiceIdConstant.CAPTCHA,
        path = ServiceContextPathConstant.RISK
)
@HttpExchange(url = "/captcha-client")
public interface CaptchaClient {

    Result<Boolean> check(@Validated CaptchaCheckReq captchaCheckReq);

}
