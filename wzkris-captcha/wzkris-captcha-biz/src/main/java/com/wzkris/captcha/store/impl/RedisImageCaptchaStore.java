package com.wzkris.captcha.store.impl;

import com.wzkris.captcha.domain.ImageCaptchaInfo;
import com.wzkris.captcha.properties.ImageCaptchaProperties;
import com.wzkris.captcha.store.ImageCaptchaStore;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisImageCaptchaStore extends AbstractRedisCaptchaStore implements ImageCaptchaStore {

    private final ImageCaptchaProperties captchaProperties;

    public RedisImageCaptchaStore(RedisTemplate<String, Object> redisTemplate, ImageCaptchaProperties captchaProperties) {
        super(redisTemplate);
        this.captchaProperties = captchaProperties;
    }

    @Override
    protected String getTokenPrefix() {
        return captchaProperties.getTokenPrefix();
    }

    @Override
    protected long getTokenExpiresMs() {
        return captchaProperties.getTokenExpiresMs();
    }

    @Override
    public void putCaptcha(String token, ImageCaptchaInfo captchaInfo) {
        redisTemplate.opsForValue().set(
                captchaProperties.getCaptchaPrefix() + token,
                captchaInfo,
                captchaProperties.getCaptchaExpiresMs(),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public ImageCaptchaInfo removeCaptcha(String token) {
        return (ImageCaptchaInfo) redisTemplate.opsForValue().getAndDelete(captchaProperties.getCaptchaPrefix() + token);
    }

}