package com.wzkris.captcha.controller;

import com.wzkris.captcha.request.CaptchaSlideRequest;
import com.wzkris.captcha.response.SlideCaptchaDataResponse;
import com.wzkris.captcha.service.SlideCaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "验证码")
@Validated
@RestController
@RequestMapping("/captcha/slide")
@RequiredArgsConstructor
public class CaptchaSlideController {

    private final SlideCaptchaService slideCaptchaService;

    @Operation(summary = "获取滑动验证码")
    @PostMapping
    public SlideCaptchaDataResponse createCaptcha() {
        return slideCaptchaService.createCaptcha();
    }

    @Operation(summary = "验证滑动验证码")
    @PostMapping("/redeem")
    public String redeem(@Validated @RequestBody CaptchaSlideRequest request) {
        return slideCaptchaService.redeem(request.getToken(), request.getX());
    }

}

