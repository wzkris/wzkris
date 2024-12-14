package com.wzkris.auth.api;

import com.wzkris.auth.api.domain.request.SmsCheckReq;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.openfeign.annotation.InnerAuth;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static com.wzkris.common.core.domain.Result.ok;

@Hidden
@InnerAuth
@RestController
@RequiredArgsConstructor
public class RemoteCaptchaApiImpl implements RemoteCaptchaApi {
    private final CaptchaService captchaService;

    @Override
    public Result<Void> validateSms(SmsCheckReq smsCheckReq) {
        captchaService.validateSmsCode(smsCheckReq.getPhoneNumber(), smsCheckReq.getSmsCode());
        return ok();
    }
}
