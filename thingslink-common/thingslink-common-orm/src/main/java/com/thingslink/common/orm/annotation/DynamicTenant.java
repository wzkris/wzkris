package com.thingslink.common.orm.annotation;


import java.lang.annotation.*;


/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 动态租户权限，提供spel动态切换租户的功能
 * @date : 2024/04/13 16:21
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicTenant {

    /**
     * 默认走自身租户
     *
     * @return spring-el表达式
     */
    String value() default "";

    /**
     * 是否启用忽略
     *
     * @return 默认不忽略
     */
    boolean enableIgnore() default false;

    /**
     * 忽略租户spel表达式，必须启用忽略才有效
     *
     * @return 默认忽略超级租户
     */
    String ignoreExpress() default "@SysUtil.isSuperTenant()";
}
