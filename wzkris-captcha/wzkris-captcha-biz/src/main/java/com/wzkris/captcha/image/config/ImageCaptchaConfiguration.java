package com.wzkris.captcha.image.config;

import com.wzkris.captcha.image.properties.ImageCaptchaProperties;
import com.wzkris.captcha.image.service.ImageCaptchaService;
import com.wzkris.captcha.image.serviceimpl.ImageCaptchaServiceImpl;
import com.wzkris.captcha.image.store.ImageCaptchaStore;
import com.wzkris.captcha.image.store.impl.RedisImageCaptchaStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class ImageCaptchaConfiguration {

    @Bean
    public ImageCaptchaStore redisImageCaptchaStore(RedisTemplate<String, Object> redisTemplate, ImageCaptchaProperties captchaProperties) {
        return new RedisImageCaptchaStore(redisTemplate, captchaProperties);
    }

    @Bean
    public ImageCaptchaService imageCaptchaService(ImageCaptchaProperties captchaProperties, ImageCaptchaStore imageCaptchaStore) {
        return new ImageCaptchaServiceImpl(captchaProperties, imageCaptchaStore);
    }

}
