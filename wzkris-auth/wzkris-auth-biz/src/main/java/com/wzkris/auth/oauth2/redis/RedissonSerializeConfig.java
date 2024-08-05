package com.wzkris.auth.oauth2.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wzkris.auth.oauth2.model.OAuth2AuthorizationModel;
import com.wzkris.auth.oauth2.redis.deserializer.OAuth2AuthorizationModelDeserializer;
import com.wzkris.common.core.utils.json.JsonUtil;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : redisson序列化支持OAuth2
 * @date : 2024/08/09 16:09
 */
@Configuration
public class RedissonSerializeConfig implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        ObjectMapper objectMapper = JsonUtil.getObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(OAuth2AuthorizationModel.class, new OAuth2AuthorizationModelDeserializer());
        objectMapper.registerModules(simpleModule);
    }

    @Bean
    public RedissonAutoConfigurationCustomizer redissonCustomizer() {
        return configuration -> configuration.setCodec(new JsonJacksonCodec(JsonUtil.getObjectMapper()));// 等jackson配置完成之后再交给redisson使用
    }

}
