package com.wzkris.captcha.config;

import com.wzkris.captcha.properties.ChallengeCaptchaProperties;
import com.wzkris.captcha.service.impl.ChallengeServiceImpl;
import com.wzkris.captcha.store.ChallengeCaptchaStore;
import com.wzkris.captcha.store.impl.RedisChallengeCaptchaStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class ChallengeCaptchaConfiguration {

    @Bean
    public ChallengeCaptchaStore challengeCaptchaStore(RedisTemplate<String, Object> redisTemplate, ChallengeCaptchaProperties captchaProperties) {
        return new RedisChallengeCaptchaStore(redisTemplate, captchaProperties);
    }

    @Bean
    public ChallengeServiceImpl challengeService(ChallengeCaptchaProperties captchaProperties, ChallengeCaptchaStore challengeCaptchaStore) {
        return new ChallengeServiceImpl(captchaProperties, challengeCaptchaStore);
    }

}