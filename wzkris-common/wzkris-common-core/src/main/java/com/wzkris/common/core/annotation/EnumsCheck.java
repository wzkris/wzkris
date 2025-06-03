package com.wzkris.common.core.annotation;

import com.wzkris.common.core.annotation.impl.validator.EnumsCheckValidator;
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
     * 枚举值
     */
    String[] values() default {};

    String message() default "{validate.enums.illegal}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
