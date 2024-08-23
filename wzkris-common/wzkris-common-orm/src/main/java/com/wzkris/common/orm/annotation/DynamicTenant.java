package com.wzkris.common.orm.annotation;


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
     * true则忽略租户，false则不处理
     * <p>
     * 解析出数字则强制走该租户
     *
     * @return 默认走租户
     */
    String enableIgnore() default "false";

    /**
     * enableIgnore参数解析类型
     * <p>
     * 0：默认，true/false字符串<p>
     * 1：纯数字，租户ID数字<p>
     * 2：spel表达式，解析结果布尔值true/false<p>
     * 3: spel表达式，解析结果租户ID数字
     */
    String parseType() default "0";
}