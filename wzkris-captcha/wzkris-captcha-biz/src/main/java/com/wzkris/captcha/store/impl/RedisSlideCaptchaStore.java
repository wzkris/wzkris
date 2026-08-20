package com.wzkris.captcha.store.impl;

import com.wzkris.captcha.domain.SlideCaptchaInfo;
import com.wzkris.captcha.properties.SlideCaptchaProperties;
import com.wzkris.captcha.store.SlideCaptchaStore;
import com.wzkris.common.redis.util.RedisJsonUtil;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisSlideCaptchaStore extends AbstractRedisCaptchaStore implements SlideCaptchaStore {

    private final SlideCaptchaProperties captchaProperties;

    public RedisSlideCaptchaStore(RedisTemplate<String, Object> redisTemplate, SlideCaptchaProperties captchaProperties) {
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
    public void putCaptcha(String token, SlideCaptchaInfo captchaInfo) {
        redisTemplate.opsForValue().set(
                captchaProperties.getCaptchaPrefix() + token,
                captchaInfo,
                captchaProperties.getCaptchaExpiresMs(),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public SlideCaptchaInfo removeCaptcha(String token) {
        return RedisJsonUtil.parse(redisTemplate.opsForValue().getAndDelete(captchaProperties.getCaptchaPrefix() + token), SlideCaptchaInfo.class);
    }

}