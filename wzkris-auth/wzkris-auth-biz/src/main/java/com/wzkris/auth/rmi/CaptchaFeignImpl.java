package com.wzkris.auth.rmi;

import com.wzkris.auth.rmi.domain.req.CaptchaCheckReq;
import com.wzkris.common.captcha.service.CaptchaService;
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
