package com.wzkris.common.openfeign.annotation;

import java.lang.annotation.*;

/**
 * 标记不走feign拦截器的客户端或方法
 *
 * @author wzkris
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FeignIgnoreInterceptor {

}
