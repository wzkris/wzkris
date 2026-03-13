package com.wzkris.auth.httpclientimpl.captcha;

import com.wzkris.auth.httpclient.captcha.CaptchaClient;
import com.wzkris.auth.httpclient.captcha.req.CaptchaCheckReq;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.common.core.model.Result;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class CaptchaClientImpl implements CaptchaClient {

    private final CaptchaService captchaService;

    @Override
    public Result<Boolean> validateCaptcha(CaptchaCheckReq captchaCheckReq) {
        return Result.ok(captchaService.validateCaptcha(captchaCheckReq.getKey(), captchaCheckReq.getCode()));
    }

}
