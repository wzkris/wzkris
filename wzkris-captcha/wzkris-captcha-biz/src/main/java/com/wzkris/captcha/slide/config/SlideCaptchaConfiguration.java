package com.wzkris.captcha.slide.config;

import com.wzkris.captcha.slide.properties.SlideCaptchaProperties;
import com.wzkris.captcha.slide.service.SlideCaptchaService;
import com.wzkris.captcha.slide.service.impl.SlideCaptchaServiceImpl;
import com.wzkris.captcha.slide.store.SlideCaptchaStore;
import com.wzkris.captcha.slide.store.impl.RedisSlideCaptchaStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class SlideCaptchaConfiguration {

    @Bean
    public SlideCaptchaStore redisSlideCaptchaStore(RedisTemplate<String, Object> redisTemplate, SlideCaptchaProperties captchaProperties) {
        return new RedisSlideCaptchaStore(redisTemplate, captchaProperties);
    }

    @Bean
    public SlideCaptchaService slideCaptchaService(SlideCaptchaProperties captchaProperties, SlideCaptchaStore slideCaptchaStore) {
        return new SlideCaptchaServiceImpl(captchaProperties, slideCaptchaStore);
    }

}