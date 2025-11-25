package com.wzkris.common.redis.config;

import com.wzkris.common.redis.aspect.GlobalCacheAspect;
import com.wzkris.common.redis.aspect.GlobalCacheEvictAspect;
import com.wzkris.common.redis.util.RedisUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({GlobalCacheAspect.class, GlobalCacheEvictAspect.class,
        RedisUtil.class})
@AutoConfiguration
public class RedisAutoConfiguration {

}
