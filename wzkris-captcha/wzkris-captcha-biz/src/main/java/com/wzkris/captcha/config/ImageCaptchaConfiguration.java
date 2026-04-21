package com.wzkris.captcha.config;

import com.wzkris.captcha.properties.ImageCaptchaProperties;
import com.wzkris.captcha.service.ImageCaptchaService;
import com.wzkris.captcha.service.impl.ImageCaptchaServiceImpl;
import com.wzkris.captcha.store.ImageCaptchaStore;
import com.wzkris.captcha.store.impl.RedisImageCaptchaStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class ImageCaptchaConfiguration {

    @Bean
    public ImageCaptchaStore imageCaptchaStore(RedisTemplate<String, Object> redisTemplate, ImageCaptchaProperties captchaProperties) {
        return new RedisImageCaptchaStore(redisTemplate, captchaProperties);
    }

    @Bean
    public ImageCaptchaService imageCaptchaService(ImageCaptchaProperties captchaProperties, ImageCaptchaStore imageCaptchaStore) {
        return new ImageCaptchaServiceImpl(captchaProperties, imageCaptchaStore);
    }

}
