package com.wzkris.auth.rmi;

import com.wzkris.auth.rmi.domain.req.SmsCheckReq;
import com.wzkris.common.captcha.service.CaptchaService;
import com.wzkris.common.core.exception.captcha.CaptchaException;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@Service
@DubboService
@RequiredArgsConstructor
public class RmiCaptchaServiceImpl implements RmiCaptchaService {

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
