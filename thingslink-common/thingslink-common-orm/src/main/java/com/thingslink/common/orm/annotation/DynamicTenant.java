package com.thingslink.common.orm.annotation;


import java.lang.annotation.*;


/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 动态租户权限，解决超级租户不走租户权限的需求
 * @date : 2024/04/13 16:21
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicTenant {

    /**
     * 默认走自身租户
     *
     * @return spring-el表达式
     */
    String value() default "";
}
