package com.wzkris.captcha.controller;

import com.wzkris.captcha.request.CaptchaImageRequest;
import com.wzkris.captcha.response.ImageCaptchaDataResponse;
import com.wzkris.captcha.service.ImageCaptchaService;
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
@RequestMapping("/captcha/image")
@RequiredArgsConstructor
public class CaptchaImageController {

    private final ImageCaptchaService imageCaptchaService;

    @Operation(summary = "获取图片验证码")
    @PostMapping
    public ImageCaptchaDataResponse createCaptcha() {
        return imageCaptchaService.createCaptcha();
    }

    @Operation(summary = "验证图片验证码")
    @PostMapping("/redeem")
    public String redeem(@Validated @RequestBody CaptchaImageRequest request) {
        return imageCaptchaService.redeem(request.getToken(), request.getCode());
    }

}

