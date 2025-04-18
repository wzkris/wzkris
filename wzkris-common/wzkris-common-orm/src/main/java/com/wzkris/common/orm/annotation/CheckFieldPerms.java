package com.wzkris.common.orm.annotation;

import java.lang.annotation.*;

/**
 * 自定义字段权限组注解
 *
 * @author wzkris
 * @since 2024/12/26 16：20
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckFieldPerms {

    /**
     * 判断权限 spel
     */
    String value();

    /**
     * 控制字段写入与读取
     */
    CheckFieldPerms.Perms rw() default Perms.ALL;

    /**
     * 校验组
     */
    Class<?>[] groups() default {};

    enum Perms {
        /**
         * A
         * 读权限
         */
        READ,
        /**
         * 写权限
         */
        WRITE,
        /**
         * 读写权限
         */
        ALL
    }
}
