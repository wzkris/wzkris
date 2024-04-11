package com.thingslink.auth.controller;

import com.thingslink.auth.service.CaptchaService;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 验证码服务
 * @date : 2023/4/17 14:42
 */
@Tag(name = "验证码")
@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class CaptchaController extends BaseController {

    private final CaptchaService captchaService;


    @Operation(summary = "图片验证码")
    @GetMapping("/code")
    public Result<Map<String, Object>> code() throws IOException {
        return success(captchaService.createPicCaptcha());
    }

    @Operation(summary = "短信验证码")
    @GetMapping("/sms_code")
    public Result<?> smsCode(@NotNull(message = "[phoneNumber] {validate.notnull}") String phoneNumber) {
        captchaService.sendSmsCode(phoneNumber);
        return success();
    }
}
