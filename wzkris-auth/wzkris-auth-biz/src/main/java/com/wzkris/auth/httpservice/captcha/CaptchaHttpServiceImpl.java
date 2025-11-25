package com.wzkris.auth.httpservice.captcha;

import com.wzkris.auth.httpservice.captcha.req.CaptchaCheckReq;
import com.wzkris.auth.service.CaptchaService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class CaptchaHttpServiceImpl implements CaptchaHttpService {

    private final CaptchaService captchaService;

    @Override
    public boolean validateCaptcha(CaptchaCheckReq captchaCheckReq) {
        return captchaService.validateCaptcha(captchaCheckReq.getKey(), captchaCheckReq.getCode());
    }

}
