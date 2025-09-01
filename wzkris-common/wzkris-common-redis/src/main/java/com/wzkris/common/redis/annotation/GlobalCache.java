package com.wzkris.common.redis.annotation;

import java.lang.annotation.*;

/**
 * 全局缓存
 *
 * @author wzkris
 * @date : 2025/08/29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GlobalCache {

    /**
     * 缓存前缀（默认为方法名）
     */
    String keyPrefix() default "";

    /**
     * 用于动态计算键的 Spring 表达式语言 （SpEL） 表达式。
     *
     * @return 会追加到前缀后
     */
    String key() default "";

    /**
     * 过期时间
     *
     * @return 毫秒
     */
    long ttl() default 0L;

    /**
     * 如果多个线程尝试加载同一键的值
     * 则只允许1个线程执行方法，其余线程会暂时阻塞直至结果被回写到缓存
     */
    boolean sync() default false;

}
