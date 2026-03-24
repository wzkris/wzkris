package com.wzkris.captcha.httpclientimpl.slide;

import com.wzkris.captcha.httpclientimpl.slide.req.SlideCaptchaReq;
import com.wzkris.captcha.slide.service.SlideCaptchaService;
import com.wzkris.common.core.model.Result;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@Hidden
@Validated
@RestController
@HttpExchange(url = "/captcha-slide-client")
@RequiredArgsConstructor
public class CaptchaSlideClientImpl {

    private final SlideCaptchaService slideCaptchaService;

    @PostExchange("/validate")
    public Result<Boolean> validateSlide(SlideCaptchaReq slideCaptchaReq) {
        Boolean b = slideCaptchaService.validateToken(slideCaptchaReq.getToken());
        return Result.ok(b);
    }

}
