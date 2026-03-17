package com.wzkris.captcha.image.store.impl;

import com.wzkris.captcha.image.domain.ImageCaptchaInfo;
import com.wzkris.captcha.image.properties.ImageCaptchaProperties;
import com.wzkris.captcha.image.store.ImageCaptchaStore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class RedisImageCaptchaStore implements ImageCaptchaStore {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ImageCaptchaProperties captchaProperties;

    @Override
    public void putCaptcha(String token, ImageCaptchaInfo captchaInfo) {
        redisTemplate.opsForValue().set(
                makeupCaptchaKey(token),
                captchaInfo,
                captchaProperties.getCaptchaExpiresMs(),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public ImageCaptchaInfo removeCaptcha(String token) {
        return (ImageCaptchaInfo) redisTemplate.opsForValue().getAndDelete(makeupCaptchaKey(token));
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