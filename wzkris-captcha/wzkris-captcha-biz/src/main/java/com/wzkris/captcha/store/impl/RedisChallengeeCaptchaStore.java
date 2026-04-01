package com.wzkris.captcha.store.impl;

import com.wzkris.captcha.domain.ChallengeCaptchaInfo;
import com.wzkris.captcha.properties.ChallengeCaptchaProperties;
import com.wzkris.captcha.store.ChallengeeCaptchaStore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class RedisChallengeeCaptchaStore implements ChallengeeCaptchaStore {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ChallengeCaptchaProperties captchaProperties;

    @Override
    public void putChallenge(String token, ChallengeCaptchaInfo challengeCaptchaInfo) {
        redisTemplate.opsForValue().set(
                makeupChallengeKey(token),
                challengeCaptchaInfo,
                captchaProperties.getChallengeExpiresMs(),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public ChallengeCaptchaInfo removeChallenge(String token) {
        return (ChallengeCaptchaInfo) redisTemplate.opsForValue().getAndDelete(makeupChallengeKey(token));
    }

    @Override
    public void putToken(String token, Date expires) {
        redisTemplate.opsForValue().set(
                makeupTokenKey(token),
                expires,
                captchaProperties.getTokenExpiresMs(),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public Date removeToken(String token) {
        return (Date) redisTemplate.opsForValue().getAndDelete(makeupTokenKey(token));
    }

    private String makeupChallengeKey(String token) {
        return captchaProperties.getChallengePrefix() + token;
    }

    private String makeupTokenKey(String token) {
        return captchaProperties.getTokenPrefix() + token;
    }

}
