package com.wzkris.captcha.config;

import com.wzkris.captcha.properties.SlideCaptchaProperties;
import com.wzkris.captcha.service.SlideCaptchaService;
import com.wzkris.captcha.service.impl.SlideCaptchaServiceImpl;
import com.wzkris.captcha.store.SlideCaptchaStore;
import com.wzkris.captcha.store.impl.RedisSlideCaptchaStore;
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
