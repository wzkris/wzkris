package com.wzkris.gateway.service.impl;

import com.wzkris.captcha.enums.CaptchaTypeEnum;
import com.wzkris.captcha.response.RiskPassExchangeResponse;
import com.wzkris.gateway.constants.GatewayRiskRedisKeys;
import com.wzkris.gateway.properties.RiskCaptchaProperties;
import com.wzkris.gateway.service.GatewayRiskPassService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GatewayRiskPassServiceImpl implements GatewayRiskPassService {

    private final StringRedisTemplate stringRedisTemplate;

    private final RiskCaptchaProperties riskCaptchaProperties;

    @Override
    public RiskPassExchangeResponse grantPass(String clientKey, CaptchaTypeEnum captchaType) {
        String passToken = UUID.randomUUID().toString().replace("-", "");
        long ttlSecs = riskCaptchaProperties.getPassTtlSeconds();
        stringRedisTemplate.opsForValue().set(
                GatewayRiskRedisKeys.passKey(passToken),
                clientKey,
                Duration.ofSeconds(ttlSecs));
        stringRedisTemplate.delete(GatewayRiskRedisKeys.lockKey(clientKey));
        OffsetDateTime expiresAt = OffsetDateTime.now(ZoneOffset.UTC).plusSeconds(ttlSecs);
        return RiskPassExchangeResponse.of(passToken, expiresAt, captchaType);
    }

}
