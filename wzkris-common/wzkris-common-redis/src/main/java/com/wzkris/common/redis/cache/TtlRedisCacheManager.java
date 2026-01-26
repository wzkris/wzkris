package com.wzkris.common.redis.cache;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * 支持动态 TTL 的 Redis Cache Manager
 * 扩展 RedisCacheManager，支持根据 TTL 动态创建 Cache 实例
 *
 * @author wzkris
 * @date 2025/01/28
 */
public class TtlRedisCacheManager extends RedisCacheManager {

    public TtlRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    public TtlRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
                                String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheNames);
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfiguration) {
        // 解析 cacheName，提取 TTL
        // 支持格式：cacheName#TTL毫秒数，例如 "userinfo#600000" 或 "userinfo#600_000"
        String[] array = StringUtils.delimitedListToStringArray(name, "#");
        String cacheName = array[0];

        if (array.length > 1) {
            // 解析 TTL（支持下划线分隔符，如 600_000）
            String ttlPart = array[1].replace("_", ""); // 移除下划线
            long ttl = Long.parseLong(ttlPart);
            if (ttl > 0) {
                cacheConfiguration = cacheConfiguration.entryTtl(Duration.ofMillis(ttl));
            }
        }

        // 使用原始的 cacheName（不包含 #TTL 部分）创建 Cache
        return super.createRedisCache(cacheName, cacheConfiguration);
    }

}
