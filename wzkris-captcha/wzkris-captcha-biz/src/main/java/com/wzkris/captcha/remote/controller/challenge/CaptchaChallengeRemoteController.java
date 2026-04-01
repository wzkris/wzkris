package com.wzkris.captcha.remote.controller.challenge;

import com.wzkris.captcha.remote.controller.challenge.request.ValidateChallengeRequest;
import com.wzkris.captcha.service.ChallengeService;
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
@RequestMapping("/captcha-challenge-remote")
@RequiredArgsConstructor
public class CaptchaChallengeRemoteController {

    private final ChallengeService challengeService;

    @PostMapping("/validate")
    public Result<Boolean> validateChallenge(@RequestBody ValidateChallengeRequest request) {
        Boolean b = challengeService.validateToken(request.getToken());
        return Result.ok(b);
    }

}



