package com.wzkris.common.captcha.config;

import com.wzkris.common.captcha.service.CapHandler;
import com.wzkris.common.captcha.service.CapService;
import com.wzkris.common.captcha.store.CapStore;
import com.wzkris.common.captcha.store.impl.InMemoryStore;
import com.wzkris.common.captcha.store.impl.RedisStore;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

/**
 * 自动配置类
 */
@EnableConfigurationProperties({CapProperties.class})
@AutoConfiguration
public class CaptchaAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "captcha", name = "store", havingValue = "redis", matchIfMissing = true)
    public CapStore redisStore(RedissonClient redissonClient, CapProperties capProperties) {
        Assert.notNull(redissonClient, "验证码组件加载失败，redis未配置！！");
        return new RedisStore(redissonClient, capProperties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "captcha", name = "store", havingValue = "memory")
    public CapStore memoryStore() {
        return new InMemoryStore();
    }

    @Bean
    @ConditionalOnBean(CapStore.class)
    public CapHandler capHandler(CapProperties capProperties, CapStore capStore) {
        return new CapHandler(capProperties, capStore);
    }

    @Bean
    @ConditionalOnBean(CapHandler.class)
    public CapService capService(CapHandler capHandler, CapProperties capProperties) {
        return new CapService(capHandler, capProperties);
    }

}
