package com.wzkris.captcha.controller;

import com.wzkris.captcha.api.CaptchaApi;
import com.wzkris.captcha.request.CaptchaDispatchRequest;
import com.wzkris.captcha.response.CaptchaDispatchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码 HTTP 唯一入口，是否携带 captcha 执行签发或换证。
 */
@Tag(name = "验证码")
@Validated
@RestController
@RequestMapping("/captcha")
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaApi captchaApi;

    @Operation(summary = "验证码派发")
    @PostMapping
    public CaptchaDispatchResponse dispatch(@RequestBody @Valid CaptchaDispatchRequest request) {
        return captchaApi.dispatch(request);
    }

}
