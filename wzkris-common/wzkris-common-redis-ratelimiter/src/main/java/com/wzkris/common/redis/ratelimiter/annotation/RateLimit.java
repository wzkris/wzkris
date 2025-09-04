package com.wzkris.common.redis.ratelimiter.annotation;

import org.redisson.api.RateType;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author wzkris
 * @date : 2025/02/17 09:01
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 限流的唯一标识（默认为方法名）
     */
    String key() default "";

    /**
     * 允许的请求速率
     */
    int rate() default 100;

    /**
     * 时间窗口的长度 单位秒
     */
    int timeWindows() default 1;

    /**
     * 限流类型
     */
    RateType rateType() default RateType.PER_CLIENT;

}
