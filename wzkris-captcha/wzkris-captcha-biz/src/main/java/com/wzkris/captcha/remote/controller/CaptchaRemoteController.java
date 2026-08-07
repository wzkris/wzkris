package com.wzkris.captcha.remote.controller;

import com.wzkris.captcha.remote.api.CaptchaRemoteApi;
import com.wzkris.captcha.remote.api.request.CaptchaCheckRequest;
import com.wzkris.common.core.model.Result;
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
@RequestMapping("/captcha-remote")
@RequiredArgsConstructor
public class CaptchaRemoteController {

    private final CaptchaRemoteApi captchaRemoteApi;

    @Operation(summary = "校验验证码")
    @PostMapping("/check")
    public Result<Boolean> check(@RequestBody @Validated CaptchaCheckRequest request) {
        return captchaRemoteApi.check(request);
    }

}
