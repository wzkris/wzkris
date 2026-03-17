package com.wzkris.captcha.challenge.store.impl;

import com.wzkris.captcha.challenge.domain.ChallengeData;
import com.wzkris.captcha.challenge.properties.ChallengeCaptchaProperties;
import com.wzkris.captcha.challenge.store.ChallengeStore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class RedisChallengeStore implements ChallengeStore {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ChallengeCaptchaProperties captchaProperties;

    @Override
    public void putChallenge(String token, ChallengeData challengeData) {
        redisTemplate.opsForValue().set(
                makeupChallengeKey(token),
                challengeData,
                captchaProperties.getChallengeExpiresMs(),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public ChallengeData removeChallenge(String token) {
        return (ChallengeData) redisTemplate.opsForValue().getAndDelete(makeupChallengeKey(token));
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
