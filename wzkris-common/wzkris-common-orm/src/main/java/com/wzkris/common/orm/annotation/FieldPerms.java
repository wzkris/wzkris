package com.wzkris.common.orm.annotation;

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
public @interface FieldPerms {

    /**
     * 分组校验
     * <p>若指定该参数则只在指定分组校验权限</p>
     */
    Class<?>[] groups() default {};
}

