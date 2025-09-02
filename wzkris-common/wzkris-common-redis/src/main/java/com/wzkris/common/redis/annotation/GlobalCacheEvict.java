package com.wzkris.common.redis.annotation;

import java.lang.annotation.*;

/**
 * 全局缓存驱逐
 *
 * @author wzkris
 * @date : 2025/08/29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GlobalCacheEvict {

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

}
