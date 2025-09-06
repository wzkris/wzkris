package com.wzkris.common.redis.ratelimiter.config;

import com.wzkris.common.redis.ratelimiter.annotation.aspect.RateLimitAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import(RateLimitAspect.class)
@AutoConfiguration
public class RatelimiterAutoConfiguration {

}
