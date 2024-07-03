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
     * 忽略租户spel表达式，true则忽略租户，false则走租户
     *
     * @return 默认忽略超级租户
     */
    String enableIgnore() default "@SysUtil.isSuperTenant()";

    /**
     * enableIgnore结果为false时，强制切换租户，默认走自身租户
     *
     * @return spring-el表达式
     */
    String forceTenant() default "";
}
