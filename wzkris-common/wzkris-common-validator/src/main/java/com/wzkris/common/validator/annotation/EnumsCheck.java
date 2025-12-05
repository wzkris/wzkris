package com.wzkris.common.validator.annotation;

import com.wzkris.common.validator.impl.EnumsCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 枚举固定值校验注解，null视为通过
 * @date : 2024/12/25 16:40
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = {EnumsCheckValidator.class})
public @interface EnumsCheck {

    /**
     * 目标枚举类
     */
    Class<? extends Enum<?>> value();

    /**
     * 要校验的属性名，默认为空表示校验枚举名称
     */
    String property() default "";

    String message() default "{invalidParameter.enums.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
