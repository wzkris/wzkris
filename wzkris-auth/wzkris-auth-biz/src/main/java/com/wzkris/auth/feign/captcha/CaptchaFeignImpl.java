package com.wzkris.auth.feign.captcha;

import com.wzkris.auth.feign.captcha.req.CaptchaCheckReq;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.common.core.exception.captcha.CaptchaException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/feign-captcha")
@RequiredArgsConstructor
public class CaptchaFeignImpl implements CaptchaFeign {

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
