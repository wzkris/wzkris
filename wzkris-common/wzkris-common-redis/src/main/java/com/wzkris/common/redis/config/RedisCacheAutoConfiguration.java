package com.wzkris.common.redis.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.wzkris.common.redis.cache.TtlRedisCacheManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

/**
 * @author wzkris
 * @see {@link org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration}
 */
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
@AutoConfiguration
public class RedisCacheAutoConfiguration {

    @Bean
    public CacheManager cacheManager(CacheProperties cacheProperties,
                                     RedisConnectionFactory redisConnectionFactory,
                                     ObjectMapper objectMapper) {
        TtlRedisCacheManager ttlRedisCacheManager;
        List<String> cacheNames = cacheProperties.getCacheNames();
        if (cacheNames.isEmpty()) {
            ttlRedisCacheManager = new TtlRedisCacheManager(
                    RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory), redisCacheConfiguration(cacheProperties, objectMapper));
        } else {
            ttlRedisCacheManager = new TtlRedisCacheManager(
                    RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                    redisCacheConfiguration(cacheProperties, objectMapper),
                    cacheNames.toArray(new String[0]));
        }
        return ttlRedisCacheManager;
    }

    public RedisCacheConfiguration redisCacheConfiguration(
            CacheProperties cacheProperties,
            ObjectMapper objectMapper) {
        return createConfiguration(cacheProperties, objectMapper);
    }

    private RedisCacheConfiguration createConfiguration(
            CacheProperties cacheProperties, ObjectMapper objectMapper) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration
                .defaultCacheConfig();

        // 创建专门用于 Redis Cache 的 ObjectMapper，启用类型信息
        ObjectMapper cacheObjectMapper = createRedisObjectMapper(objectMapper);

        config = config
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(cacheObjectMapper)));
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }

    private ObjectMapper createRedisObjectMapper(ObjectMapper objectMapper) {
        // 创建专门用于 Redis 的 ObjectMapper，启用类型信息
        ObjectMapper redisObjectMapper = objectMapper.copy();
        redisObjectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        return redisObjectMapper;
    }

}
