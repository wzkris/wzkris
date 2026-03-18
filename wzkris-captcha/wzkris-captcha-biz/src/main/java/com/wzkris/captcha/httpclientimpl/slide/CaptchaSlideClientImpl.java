package com.wzkris.captcha.httpclientimpl.slide;

import com.wzkris.captcha.httpclient.slide.CaptchaSlideClient;
import com.wzkris.captcha.httpclient.slide.req.SlideCaptchaReq;
import com.wzkris.captcha.slide.service.SlideCaptchaService;
import com.wzkris.common.core.model.Result;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@Validated
@RestController
@RequiredArgsConstructor
public class CaptchaSlideClientImpl implements CaptchaSlideClient {

    private final SlideCaptchaService slideCaptchaService;

    @Override
    public Result<Boolean> validateSlide(SlideCaptchaReq slideCaptchaReq) {
        Boolean b = slideCaptchaService.validateToken(slideCaptchaReq.getToken());
        return Result.ok(b);
    }

}
