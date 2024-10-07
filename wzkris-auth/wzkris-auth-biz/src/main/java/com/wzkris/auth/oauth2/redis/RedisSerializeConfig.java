package com.wzkris.auth.oauth2.redis;

import com.wzkris.auth.oauth2.utils.OAuth2JsonUtil;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : redisson序列化支持OAuth2
 * @date : 2024/08/19 17:06
 */
@Configuration
public class RedisSerializeConfig {

    /**
     * redisson客户端序列化配置
     */
    @Bean
    public RedissonAutoConfigurationCustomizer redissonCustomizer() {
        return configuration -> configuration.setCodec(new JsonJacksonCodec(OAuth2JsonUtil.getObjectMapper()));// 等jackson配置完成之后再交给redisson使用
    }

    /**
     * springSession序列化配置
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(OAuth2JsonUtil.getObjectMapper());
    }
}
