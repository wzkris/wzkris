package com.wzkris.captcha.httpclient.image;

import com.wzkris.captcha.httpclient.image.req.ImageCaptchaReq;
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
@HttpExchange(url = "/captcha-image-client")
public interface CaptchaImageClient {

    Result<Boolean> validateImage(@Validated ImageCaptchaReq req);

}