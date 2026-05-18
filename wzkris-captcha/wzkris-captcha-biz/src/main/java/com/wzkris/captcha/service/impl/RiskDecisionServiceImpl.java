package com.wzkris.captcha.service.impl;

import com.wzkris.captcha.enums.CaptchaTypeEnum;
import com.wzkris.captcha.request.RiskPassExchangeRequest;
import com.wzkris.captcha.service.ChallengeService;
import com.wzkris.captcha.service.ImageCaptchaService;
import com.wzkris.captcha.service.RiskDecisionService;
import com.wzkris.captcha.service.SlideCaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RiskDecisionServiceImpl implements RiskDecisionService {

    private final ImageCaptchaService imageCaptchaService;

    private final SlideCaptchaService slideCaptchaService;

    private final ChallengeService challengeService;

    @Override
    public boolean validateExchange(RiskPassExchangeRequest request) {
        return Boolean.TRUE.equals(validateToken(request.getCaptchaType(), request.getCaptchaToken().trim()));
    }

    private Boolean validateToken(CaptchaTypeEnum captchaType, String token) {
        return switch (Objects.requireNonNull(captchaType)) {
            case CHALLENGE -> challengeService.validateToken(token);
            case IMAGE -> imageCaptchaService.validateToken(token);
            case SLIDE -> slideCaptchaService.validateToken(token);
        };
    }

}
