package com.wzkris.risk.httpclientimpl.captcha;

import com.wzkris.common.core.model.Result;
import com.wzkris.risk.httpclient.captcha.CaptchaClient;
import com.wzkris.risk.httpclient.captcha.req.CaptchaSmsCodeReq;
import com.wzkris.risk.httpclient.captcha.req.CaptchaSmsValidateReq;
import com.wzkris.risk.service.captcha.CaptchaFacadeService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@Validated
@RestController
@RequiredArgsConstructor
public class CaptchaClientImpl implements CaptchaClient {

    private final CaptchaFacadeService captchaFacadeService;

    @Override
    public Result<Integer> smscode(@Valid CaptchaSmsCodeReq request) {
        return captchaFacadeService.sendSms(request);
    }

    @Override
    public Result<Boolean> validateSms(@Valid CaptchaSmsValidateReq request) {
        return captchaFacadeService.validateSms(request);
    }

}
