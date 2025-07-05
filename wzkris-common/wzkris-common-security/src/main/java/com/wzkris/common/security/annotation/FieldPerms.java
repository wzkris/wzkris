package com.wzkris.common.security.annotation;

import com.wzkris.common.security.annotation.enums.Rw;

import java.lang.annotation.*;

/**
 * 自定义字段权限组注解
 *
 * @author wzkris
 * @since 2024/12/26 16：20
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldPerms {

    /**
     * 控制字段写入与读取
     */
    Rw[] rw() default {Rw.READ, Rw.WRITE};

}
