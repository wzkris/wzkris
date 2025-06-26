package com.wzkris.common.core.annotation;

import com.wzkris.common.core.annotation.impl.validator.IdCardValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 身份证注解
 * @date : 2023/12/15 10:49
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = {IdCardValidator.class})
public @interface IdCard {

    String message() default "{validate.idcard.illegal}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
