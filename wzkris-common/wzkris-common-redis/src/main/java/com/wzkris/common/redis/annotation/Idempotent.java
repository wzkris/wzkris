package com.wzkris.common.redis.annotation;

import java.lang.annotation.*;

/**
 * 通用幂等注解
 *
 * @author wzkris
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

    /**
     * 幂等键SpEL表达式，支持 #p0/#a0/参数名 等写法
     * 为空时默认使用方法全限定名
     */
    String key() default "";

    /**
     * 结果缓存时长（秒）
     */
    long ttlSeconds() default 15;

}
