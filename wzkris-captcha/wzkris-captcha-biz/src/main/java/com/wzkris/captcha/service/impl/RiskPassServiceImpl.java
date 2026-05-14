package com.wzkris.captcha.service.impl;

import com.wzkris.captcha.enums.CaptchaTypeEnum;
import com.wzkris.captcha.properties.RiskPassProperties;
import com.wzkris.captcha.request.RiskPassExchangeRequest;
import com.wzkris.captcha.response.RiskPassExchangeResponse;
import com.wzkris.captcha.risk.RiskClientKeys;
import com.wzkris.captcha.service.ChallengeService;
import com.wzkris.captcha.service.ImageCaptchaService;
import com.wzkris.captcha.service.RiskPassService;
import com.wzkris.captcha.service.SlideCaptchaService;
import com.wzkris.common.core.model.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RiskPassServiceImpl implements RiskPassService {

    private final ImageCaptchaService imageCaptchaService;

    private final SlideCaptchaService slideCaptchaService;

    private final ChallengeService challengeService;

    private final StringRedisTemplate stringRedisTemplate;

    private final RiskPassProperties riskPassProperties;

    @Override
    public Result<RiskPassExchangeResponse> exchange(RiskPassExchangeRequest request) {
        String token = request.getCaptchaToken().trim();
        CaptchaTypeEnum captchaType = request.getCaptchaType();

        if (!Boolean.TRUE.equals(validateToken(captchaType, token))) {
            return Result.requestFail("invalidParameter.captcha.error");
        }

        var attrs = RequestContextHolder.getRequestAttributes();
        if (!(attrs instanceof ServletRequestAttributes servletAttrs)) {
            return Result.systemError("missing servlet request context");
        }
        String clientKey = RiskClientKeys.defaultCompositeKey(servletAttrs.getRequest());

        return Result.ok(grantPass(clientKey, captchaType));
    }

    private RiskPassExchangeResponse grantPass(String clientKey, CaptchaTypeEnum captchaType) {
        String passToken = UUID.randomUUID().toString().replace("-", "");
        long ttlSecs = riskPassProperties.getPassTtlSeconds();

        stringRedisTemplate.opsForValue().set(
                riskPassProperties.getPassKeyPrefix() + passToken,
                clientKey,
                Duration.ofSeconds(ttlSecs));
        stringRedisTemplate.delete(riskPassProperties.getLockKeyPrefix() + clientKey);

        OffsetDateTime expiresAt = OffsetDateTime.now(ZoneOffset.UTC).plusSeconds(ttlSecs);
        return RiskPassExchangeResponse.of(passToken, expiresAt, captchaType);
    }

    private Boolean validateToken(CaptchaTypeEnum captchaType, String token) {
        return switch (Objects.requireNonNull(captchaType)) {
            case CHALLENGE -> challengeService.validateToken(token);
            case IMAGE -> imageCaptchaService.validateToken(token);
            case SLIDE -> slideCaptchaService.validateToken(token);
        };
    }
}
