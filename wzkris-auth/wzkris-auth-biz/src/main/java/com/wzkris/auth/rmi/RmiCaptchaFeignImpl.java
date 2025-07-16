package com.wzkris.auth.rmi;

import com.wzkris.auth.rmi.domain.req.SmsCheckReq;
import com.wzkris.common.captcha.service.CaptchaService;
import com.wzkris.common.core.exception.captcha.CaptchaException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class RmiCaptchaFeignImpl implements RmiCaptchaFeign {

    private final CaptchaService captchaService;

    @Override
    public boolean validateSms(SmsCheckReq smsCheckReq) {
        try {
            captchaService.validateCaptcha(smsCheckReq.getPhoneNumber(), smsCheckReq.getSmsCode());
            return true;
        } catch (CaptchaException ignored) {
        }
        return false;
    }

}
