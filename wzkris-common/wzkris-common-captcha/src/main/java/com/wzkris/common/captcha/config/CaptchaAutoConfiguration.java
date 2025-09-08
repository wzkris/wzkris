package com.wzkris.common.captcha.config;

import com.wzkris.common.captcha.handler.CapHandler;
import com.wzkris.common.captcha.properties.CapProperties;
import com.wzkris.common.captcha.service.CaptchaService;
import com.wzkris.common.captcha.store.CapStore;
import com.wzkris.common.captcha.store.impl.InMemoryStore;
import com.wzkris.common.captcha.store.impl.RedisStore;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 自动配置类
 */
@Import({CapProperties.class, CaptchaService.class, CapHandler.class})
@AutoConfiguration
public class CaptchaAutoConfiguration {

    @Bean
    @ConditionalOnClass(RedissonClient.class)
    public CapStore redisStore(RedissonClient redissonClient, CapProperties capProperties) {
        return new RedisStore(redissonClient, capProperties);
    }

    @Bean
    @ConditionalOnMissingBean(CapStore.class)
    public CapStore memoryStore() {
        return new InMemoryStore();
    }

}
