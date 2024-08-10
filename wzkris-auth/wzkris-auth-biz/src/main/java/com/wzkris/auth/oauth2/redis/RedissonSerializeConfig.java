package com.wzkris.auth.oauth2.redis;

import com.wzkris.common.core.utils.json.JsonUtil;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : redisson序列化支持OAuth2
 * @date : 2024/08/09 16:09
 */
@Configuration
public class RedissonSerializeConfig {

    @Bean
    public RedissonAutoConfigurationCustomizer redissonCustomizer() {
        return configuration -> configuration.setCodec(new JsonJacksonCodec(JsonUtil.getObjectMapper()));// 等jackson配置完成之后再交给redisson使用
    }

}
