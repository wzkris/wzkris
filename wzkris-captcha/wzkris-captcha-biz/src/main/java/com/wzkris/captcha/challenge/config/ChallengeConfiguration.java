package com.wzkris.captcha.challenge.config;

import com.wzkris.captcha.challenge.properties.ChallengeCaptchaProperties;
import com.wzkris.captcha.challenge.service.impl.ChallengeServiceImpl;
import com.wzkris.captcha.challenge.store.ChallengeStore;
import com.wzkris.captcha.challenge.store.impl.RedisChallengeStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class ChallengeConfiguration {

    @Bean
    public ChallengeStore redisStore(RedisTemplate<String, Object> redisTemplate, ChallengeCaptchaProperties captchaProperties) {
        return new RedisChallengeStore(redisTemplate, captchaProperties);
    }

    @Bean
    public ChallengeServiceImpl challengeHandler(ChallengeCaptchaProperties captchaProperties, ChallengeStore challengeStore) {
        return new ChallengeServiceImpl(captchaProperties, challengeStore);
    }

}
