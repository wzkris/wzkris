package com.wzkris.auth.controller;

import com.wzkris.auth.domain.dto.KaptchaDTO;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.common.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.wzkris.common.core.domain.Result.ok;

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
public class CaptchaController {

    private final CaptchaService captchaService;

    @Operation(summary = "图片验证码")
    @GetMapping("/code")
    public Result<KaptchaDTO> code() throws IOException {
        KaptchaDTO picCaptcha = captchaService.createPicCaptcha();

        return ok(picCaptcha);
    }

    @Operation(summary = "短信验证码")
    @GetMapping("/sms_code")
    public Result<Void> smsCode(@NotBlank(message = "[phone] {validate.notnull}")
                                @Length(min = 11, max = 11, message = "[phone] {validate.size.illegal}")
                                String phone) {
        captchaService.sendSmsCode(phone);
        return Result.ok();
    }
}
