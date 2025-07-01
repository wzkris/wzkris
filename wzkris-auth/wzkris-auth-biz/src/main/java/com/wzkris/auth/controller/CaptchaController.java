package com.wzkris.auth.controller;

import com.wzkris.auth.domain.req.SmsCodeReq;
import com.wzkris.common.captcha.request.RedeemChallengeRequest;
import com.wzkris.common.captcha.response.ChallengeResponse;
import com.wzkris.common.captcha.response.RedeemChallengeResponse;
import com.wzkris.common.captcha.service.CaptchaService;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.redis.annotation.RateLimit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.wzkris.common.core.domain.Result.err412;
import static com.wzkris.common.core.domain.Result.ok;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 验证码服务
 * @date : 2023/4/17 14:42
 */
@Tag(name = "验证码")
@Slf4j
@Validated
@RestController
@RequestMapping("/captcha")
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaService captchaService;

    @RateLimit
    @Operation(summary = "获取挑战")
    @PostMapping("/challenge")
    public ChallengeResponse challenge() {
        return captchaService.createChallenge();
    }

    @Operation(summary = "验证挑战")
    @PostMapping("/redeem")
    public RedeemChallengeResponse redeem(@RequestBody @Valid RedeemChallengeRequest redeemChallengeRequest) {
        return captchaService.redeemChallenge(redeemChallengeRequest);
    }

    @Operation(summary = "短信验证码")
    @PostMapping("/sms_code")
    public Result<Integer> sendSms(@RequestBody @Valid SmsCodeReq req) {
        boolean valid = captchaService.validateToken(req.getCaptchaId());
        if (!valid) {
            return err412("验证码异常");
        }
        captchaService.validateMaxTry(req.getPhone(), 1, 120);
        // TODO 发送短信
        String code = String.valueOf(RandomUtils.secure().randomInt(100_000, 999_999));
        log.info("手机号：{} 验证码是：{}", req.getPhone(), code);
        captchaService.setCaptcha(req.getPhone(), code);
        return ok();
    }

}
