package com.wzkris.captcha.remote.controller.risk;

import com.wzkris.captcha.request.ValidateChallengeRequest;
import com.wzkris.captcha.request.RiskPassExchangeRequest;
import com.wzkris.captcha.service.ChallengeService;
import com.wzkris.captcha.service.RiskDecisionService;
import com.wzkris.common.core.model.Result;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@Validated
@RestController
@RequestMapping("/risk-captcha-remote")
@RequiredArgsConstructor
public class RiskCaptchaRemoteController {

    private final RiskDecisionService riskDecisionService;

    private final ChallengeService challengeService;

    @PostMapping("/validate-exchange")
    public Result<Boolean> validateExchange(@RequestBody @Validated RiskPassExchangeRequest request) {
        return Result.ok(riskDecisionService.validateExchange(request));
    }

    @PostMapping("/validate-challenge")
    public Result<Boolean> validateChallenge(@RequestBody @Validated ValidateChallengeRequest request) {
        return Result.ok(challengeService.validateToken(request.getToken()));
    }

}
