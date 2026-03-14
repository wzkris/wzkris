package com.wzkris.risk.httpclient.captcha;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.httpclient.annotation.HttpClient;
import com.wzkris.common.httpclient.constants.ServiceContextPathConstant;
import com.wzkris.common.httpclient.constants.ServiceIdConstant;
import com.wzkris.risk.httpclient.captcha.req.CaptchaSmsCodeReq;
import com.wzkris.risk.httpclient.captcha.req.CaptchaSmsValidateReq;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpClient(
        serviceId = ServiceIdConstant.RISK,
        path = ServiceContextPathConstant.RISK
)
@HttpExchange(url = "/captcha-client")
public interface CaptchaClient {

    @PostExchange("/smscode")
    Result<Integer> smscode(@RequestBody CaptchaSmsCodeReq request);

    @PostExchange("/validate-sms")
    Result<Boolean> validateSms(@RequestBody CaptchaSmsValidateReq request);

}
