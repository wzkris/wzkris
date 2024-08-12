package com.wzkris.auth.oauth2.redis;

import org.redisson.codec.JsonJacksonCodec;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : redisson序列化支持OAuth2
 * @date : 2024/08/19 17:06
 */
@Configuration
public class RedissonSerializeConfig {

    @Bean
    public RedissonAutoConfigurationCustomizer redissonCustomizer() {
        return configuration -> configuration.setCodec(new JsonJacksonCodec(OAuth2JsonUtil.getObjectMapper()));// 等jackson配置完成之后再交给redisson使用
    }

}
