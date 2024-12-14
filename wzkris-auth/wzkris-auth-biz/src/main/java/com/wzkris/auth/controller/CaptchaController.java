package com.wzkris.auth.controller;

import com.wzkris.auth.domain.req.SmsCodeReq;
import com.wzkris.auth.domain.vo.KaptchaVO;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.common.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Result<KaptchaVO> code() throws IOException {
        KaptchaVO picCaptcha = captchaService.createPicCode();

        return ok(picCaptcha);
    }

    @Operation(summary = "短信验证码")
    @PostMapping("/sms_code")
    public Result<Integer> smsCode(@RequestBody @Valid SmsCodeReq req) {
        captchaService.validatePicCode(req.getUuid(), req.getCode());
        return ok(captchaService.createSmsCode(req.getPhone()));
    }
}
