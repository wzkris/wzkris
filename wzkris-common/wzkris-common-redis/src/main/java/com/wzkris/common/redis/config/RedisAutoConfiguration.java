package com.wzkris.common.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.redis.aspect.IdempotentAspect;
import com.wzkris.common.redis.util.DistLockTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Import({DistLockTemplate.class})
@AutoConfiguration(before = org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class)
public class RedisAutoConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        template.setDefaultSerializer(new StringRedisSerializer());

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(createRedisObjectMapper());
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);

        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public IdempotentAspect idempotentAspect(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        return new IdempotentAspect(redisTemplate, objectMapper);
    }

    /**
     * 创建专门用于 Redis 的 ObjectMapper。
     * <p>
     * 不启用 DefaultTyping：值以 clean JSON 存储（无 @class 类型标识），
     * 避免类重命名/移动后旧数据因类型标识失效而反序列化失败；读取时由
     * {@link com.wzkris.common.redis.util.RedisJsonUtil} 显式指定目标类型。
     */
    private ObjectMapper createRedisObjectMapper() {
        ObjectMapper redisObjectMapper = JsonUtil.getObjectMapper().copy();
        return redisObjectMapper;
    }

}
