package com.wzkris.risk.service.captcha;

import com.wzkris.common.core.exception.request.TooManyRequestException;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.risk.captcha.CaptchaAbility;
import com.wzkris.risk.captcha.CaptchaAbilityRegistry;
import com.wzkris.risk.captcha.challenge.properties.ChallengeCaptchaProperties;
import com.wzkris.risk.domain.dto.ChallengeData;
import com.wzkris.risk.domain.req.RedeemChallengeReq;
import com.wzkris.risk.domain.resp.RedeemChallengeResp;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

@Service
public class CaptchaService {

    private static final String SMS_MAXTRY_PREFIX = "risk:sms:max_try:";

    private static final String DEFAULT_CAPTCHA_TYPE = "challenge";

    private final CaptchaAbilityRegistry captchaAbilityRegistry;

    private final ChallengeCaptchaProperties challengeCaptchaProperties;

    private final StringRedisTemplate redisTemplate;

    public CaptchaService(CaptchaAbilityRegistry captchaAbilityRegistry,
                          ChallengeCaptchaProperties challengeCaptchaProperties,
                          StringRedisTemplate redisTemplate) {
        this.captchaAbilityRegistry = captchaAbilityRegistry;
        this.challengeCaptchaProperties = challengeCaptchaProperties;
        this.redisTemplate = redisTemplate;
    }

    public ChallengeData createChallenge() {
        return createChallenge(null);
    }

    public ChallengeData createChallenge(String type) {
        return currentAbility(type).createChallenge();
    }

    public RedeemChallengeResp redeem(RedeemChallengeReq request) {
        String type = resolveType(request.getType());
        return captchaAbilityRegistry.get(type).redeem(request);
    }

    public Boolean validateChallenge(String token) {
        return validateChallenge(token, null);
    }

    public Boolean validateChallenge(String token, String type) {
        return currentAbility(type).validateToken(token);
    }

    public void setCaptcha(String key, String code) {
        redisTemplate.opsForValue().set(
                challengeCaptchaProperties.getChallengePrefix() + key,
                code,
                Duration.ofMillis(challengeCaptchaProperties.getTokenExpiresMs()));
    }

    public boolean validateSmsCode(String phone, String code) {
        if (StringUtil.isAnyBlank(phone, code)) {
            return false;
        }
        String redisCode = redisTemplate.opsForValue().get(challengeCaptchaProperties.getChallengePrefix() + phone);
        return StringUtil.equals(redisCode, code);
    }

    public void validateSmsMaxTry(String phone, int maxTry, int timeout) {
        String counterKey = SMS_MAXTRY_PREFIX + phone;
        String luaScript = "local currentTry = redis.call('get', KEYS[1]) or 0 "
                + "if tonumber(currentTry) >= tonumber(ARGV[1]) then "
                + "    return 0 "
                + "else "
                + "    redis.call('incr', KEYS[1]) "
                + "    redis.call('expire', KEYS[1], ARGV[2]) "
                + "    return 1 "
                + "end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(luaScript);
        redisScript.setResultType(Long.class);
        Long result = redisTemplate.execute(redisScript,
                Collections.singletonList(counterKey),
                maxTry,
                timeout);
        if (result == null || result.intValue() == 0) {
            throw new TooManyRequestException();
        }
    }

    private CaptchaAbility currentAbility(String type) {
        return captchaAbilityRegistry.get(resolveType(type));
    }

    private String resolveType(String type) {
        return StringUtil.isBlank(type) ? DEFAULT_CAPTCHA_TYPE : type;
    }

}
