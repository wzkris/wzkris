package com.wzkris.captcha.config;

import com.wzkris.captcha.properties.ChallengeCaptchaProperties;
import com.wzkris.captcha.service.impl.ChallengeServiceImpl;
import com.wzkris.captcha.store.ChallengeeCaptchaStore;
import com.wzkris.captcha.store.impl.RedisChallengeeCaptchaStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class ChallengeCaptchaConfiguration {

    @Bean
    public ChallengeeCaptchaStore challengeeCaptchaStore(RedisTemplate<String, Object> redisTemplate, ChallengeCaptchaProperties captchaProperties) {
        return new RedisChallengeeCaptchaStore(redisTemplate, captchaProperties);
    }

    @Bean
    public ChallengeServiceImpl challengeService(ChallengeCaptchaProperties captchaProperties, ChallengeeCaptchaStore challengeeCaptchaStore) {
        return new ChallengeServiceImpl(captchaProperties, challengeeCaptchaStore);
    }

}
