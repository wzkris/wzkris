package com.wzkris.captcha.controller;

import com.wzkris.captcha.domain.ChallengeCaptchaInfo;
import com.wzkris.captcha.request.RedeemChallengeRequest;
import com.wzkris.captcha.response.RedeemChallengeResponse;
import com.wzkris.captcha.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "验证码")
@Validated
@RestController
@RequestMapping("/captcha")
@RequiredArgsConstructor
public class CaptchaChallengeController {

    private final ChallengeService challengeService;

    @Operation(summary = "获取挑战")
    @PostMapping("/challenge")
    public ChallengeCaptchaInfo createChallenge() {
        return challengeService.createChallenge();
    }

    @Operation(summary = "验证挑战")
    @PostMapping("/redeem")
    public RedeemChallengeResponse redeem(@RequestBody @Valid RedeemChallengeRequest request) {
        return challengeService.redeem(request.getToken(), request.getSolutions());
    }

}

