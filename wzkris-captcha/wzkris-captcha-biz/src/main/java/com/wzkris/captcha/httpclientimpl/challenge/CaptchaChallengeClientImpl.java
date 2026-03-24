package com.wzkris.captcha.httpclientimpl.challenge;

import com.wzkris.captcha.challenge.service.ChallengeService;
import com.wzkris.captcha.httpclientimpl.challenge.req.ValidateChallengeReq;
import com.wzkris.common.core.model.Result;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@Validated
@RestController
@RequestMapping("/captcha-challenge-client")
@RequiredArgsConstructor
public class CaptchaChallengeClientImpl {

    private final ChallengeService challengeService;

    @PostMapping("/validate")
    public Result<Boolean> validateChallenge(ValidateChallengeReq challengeReq) {
        Boolean b = challengeService.validateToken(challengeReq.getToken());
        return Result.ok(b);
    }

}
