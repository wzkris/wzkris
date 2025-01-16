package com.wzkris.common.orm.annotation;

import java.lang.annotation.*;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 忽略租户隔离，优先级最高，标记后方法或类内任何切换租户无效
 * @since : 2024/12/31 11:10
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreTenant {

    /**
     * 默认忽略
     */
    boolean value() default true;

    /**
     * 解析租户ID的表达式，value为false时生效
     */
    String forceTenantId() default "";
}