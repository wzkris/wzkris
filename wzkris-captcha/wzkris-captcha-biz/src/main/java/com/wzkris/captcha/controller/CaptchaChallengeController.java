package com.wzkris.captcha.controller;

import com.wzkris.captcha.challenge.domain.ChallengeData;
import com.wzkris.captcha.challenge.domain.RedeemChallengeResult;
import com.wzkris.captcha.challenge.domain.req.RedeemChallengeReq;
import com.wzkris.captcha.challenge.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "验证码")
@Validated
@RestController
@RequestMapping("/captcha/challenge")
@RequiredArgsConstructor
public class CaptchaChallengeController {

    private final ChallengeService challengeService;

    @Operation(summary = "获取挑战")
    @GetMapping
    public ChallengeData challenge() {
        return challengeService.createChallenge();
    }

    @Operation(summary = "验证挑战")
    @PostMapping("/redeem")
    public RedeemChallengeResult redeem(@RequestBody @Valid RedeemChallengeReq request) {
        return challengeService.redeem(request.getToken(), request.getSolutions());
    }

}
