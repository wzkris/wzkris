package com.wzkris.auth.httpclient.captcha;

import com.wzkris.auth.httpclient.captcha.req.CaptchaCheckReq;
import com.wzkris.auth.service.CaptchaService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class CaptchaClientImpl implements CaptchaClient {

    private final CaptchaService captchaService;

    @Override
    public boolean validateCaptcha(CaptchaCheckReq captchaCheckReq) {
        return captchaService.validateCaptcha(captchaCheckReq.getKey(), captchaCheckReq.getCode());
    }

}
