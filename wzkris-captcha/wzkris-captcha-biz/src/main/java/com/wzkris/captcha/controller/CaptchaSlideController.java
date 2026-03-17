package com.wzkris.captcha.controller;

import com.wzkris.captcha.slide.domain.SlideCaptchaData;
import com.wzkris.captcha.slide.domain.req.CaptchaSlideReq;
import com.wzkris.captcha.slide.service.SlideCaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "验证码")
@Validated
@RestController
@RequestMapping("/captcha/slide")
@RequiredArgsConstructor
public class CaptchaSlideController {

    private final SlideCaptchaService slideCaptchaService;

    @Operation(summary = "获取滑动验证码")
    @GetMapping
    public SlideCaptchaData getCaptcha() {
        return slideCaptchaService.createCaptcha();
    }

    @Operation(summary = "验证滑动验证码")
    @PostMapping("/redeem")
    public String redeem(@Validated @RequestBody CaptchaSlideReq req) {
        return slideCaptchaService.redeem(req.getToken(), req.getX());
    }

}