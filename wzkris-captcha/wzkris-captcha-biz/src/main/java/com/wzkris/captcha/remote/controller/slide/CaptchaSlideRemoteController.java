package com.wzkris.captcha.remote.controller.slide;

import com.wzkris.captcha.remote.controller.slide.request.SlideCaptchaRequest;
import com.wzkris.captcha.service.SlideCaptchaService;
import com.wzkris.common.core.model.Result;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@Hidden
@Validated
@RestController
@HttpExchange(url = "/captcha-slide-remote")
@RequiredArgsConstructor
public class CaptchaSlideRemoteController {

    private final SlideCaptchaService slideCaptchaService;

    @PostExchange("/validate")
    public Result<Boolean> validateSlide(@RequestBody SlideCaptchaRequest request) {
        Boolean b = slideCaptchaService.validateToken(request.getToken());
        return Result.ok(b);
    }

}



