package com.wzkris.risk.captcha.challenge.config;

import com.wzkris.risk.captcha.challenge.properties.ChallengeCaptchaProperties;
import com.wzkris.risk.captcha.challenge.service.ChallengeHandler;
import com.wzkris.risk.captcha.challenge.store.ChallengeStore;
import com.wzkris.risk.captcha.challenge.store.impl.DefaultChallengeStore;
import com.wzkris.risk.captcha.challenge.store.impl.RedisChallengeStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
    @ConditionalOnMissingBean(ChallengeStore.class)
    public ChallengeStore defaultStore() {
        return new DefaultChallengeStore();
    }

    @Bean
    public ChallengeHandler challengeHandler(ChallengeCaptchaProperties captchaProperties, ChallengeStore challengeStore) {
        return new ChallengeHandler(captchaProperties, challengeStore);
    }

}
