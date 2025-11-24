package com.wzkris.auth.httpservice.captcha;

import com.wzkris.auth.httpservice.captcha.req.CaptchaCheckReq;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.common.core.exception.captcha.CaptchaException;
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
        try {
            captchaService.validateCaptcha(captchaCheckReq.getKey(), captchaCheckReq.getCode());
            return true;
        } catch (CaptchaException ignored) {
        }
        return false;
    }

}
