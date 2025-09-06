package com.wzkris.common.redis.config;

import com.wzkris.common.redis.annotation.aspect.GlobalCacheAspect;
import com.wzkris.common.redis.annotation.aspect.GlobalCacheEvictAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({GlobalCacheAspect.class, GlobalCacheEvictAspect.class})
@AutoConfiguration
public class RedisAutoConfiguration {

}
