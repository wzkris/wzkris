package com.wzkris.auth.api;

import com.wzkris.auth.api.domain.request.SmsCheckReq;
import com.wzkris.common.captcha.service.CaptchaService;
import com.wzkris.common.core.exception.captcha.CaptchaException;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@Service
@DubboService
@RequiredArgsConstructor
public class RemoteCaptchaApiImpl implements RemoteCaptchaApi {

    private final CaptchaService captchaService;

    @Override
    public boolean validateSms(SmsCheckReq smsCheckReq) throws CaptchaException {
        try {
            captchaService.validateCaptcha(smsCheckReq.getPhoneNumber(), smsCheckReq.getSmsCode());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
