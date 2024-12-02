package com.wzkris.auth.api;

import com.wzkris.auth.api.domain.dto.SmsDTO;
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
    public Result<Void> validateSms(SmsDTO smsDTO) {
        captchaService.validateSmsCode(smsDTO.getPhoneNumber(), smsDTO.getSmsCode());
        return ok();
    }
}
