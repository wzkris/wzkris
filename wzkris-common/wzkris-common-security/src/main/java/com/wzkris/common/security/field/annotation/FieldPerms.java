package com.wzkris.common.security.field.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wzkris.common.security.field.DefaultFieldJsonDeserializer;
import com.wzkris.common.security.field.DefaultFieldJsonSerializer;

import java.lang.annotation.*;

/**
 * 自定义字段权限注解
 *
 * @author wzkris
 * @since 2024/12/18 9：40
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = DefaultFieldJsonSerializer.class)
@JsonDeserialize(using = DefaultFieldJsonDeserializer.class)
public @interface FieldPerms {

    /**
     * 控制字段写入与读取
     */
    Perms perms() default Perms.ALL;

    /**
     * 判断权限 spel
     */
    String value();

    public enum Perms {
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

