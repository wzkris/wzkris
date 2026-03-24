package com.wzkris.captcha.slide.store.impl;

import com.wzkris.captcha.slide.domain.SlideCaptchaInfo;
import com.wzkris.captcha.slide.properties.SlideCaptchaProperties;
import com.wzkris.captcha.slide.store.SlideCaptchaStore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class RedisSlideCaptchaStore implements SlideCaptchaStore {

    private final RedisTemplate<String, Object> redisTemplate;

    private final SlideCaptchaProperties captchaProperties;

    @Override
    public void putCaptcha(String token, SlideCaptchaInfo captchaInfo) {
        redisTemplate.opsForValue().set(
                makeupCaptchaKey(token),
                captchaInfo,
                captchaProperties.getCaptchaExpiresMs(),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public SlideCaptchaInfo removeCaptcha(String token) {
        return (SlideCaptchaInfo) redisTemplate.opsForValue().getAndDelete(makeupCaptchaKey(token));
    }

    @Override
    public void putToken(String tokenKey, Date expires) {
        redisTemplate.opsForValue().set(
                makeupTokenKey(tokenKey),
                expires,
                captchaProperties.getTokenExpiresMs(),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public Date removeToken(String tokenKey) {
        return (Date) redisTemplate.opsForValue().getAndDelete(makeupTokenKey(tokenKey));
    }

    private String makeupCaptchaKey(String token) {
        return captchaProperties.getCaptchaPrefix() + token;
    }

    private String makeupTokenKey(String token) {
        return captchaProperties.getTokenPrefix() + token;
    }

}
