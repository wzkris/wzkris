package com.wzkris.common.orm.annotation;


import java.lang.annotation.*;


/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 忽略租户隔离，优先级最高，标记后方法或类内任何切换租户无效
 * @date : 2024/04/13 16:21
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreTenant {

}