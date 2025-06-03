package com.wzkris.common.redis.config;

import com.wzkris.common.redis.manager.PlusSpringCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

/**
 * redis配置
 *
 * @author Lion Li
 */
@Slf4j
@EnableCaching
public class CacheConfig {

    /**
     * 自定义缓存管理器 整合spring-cache
     */
    @Bean
    public CacheManager cacheManager() {
        return new PlusSpringCacheManager();
    }
}
