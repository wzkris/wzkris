package com.thingslink.common.core.validate;

import com.thingslink.common.core.validate.impl.PhoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 手机号注解
 * @date : 2023/12/14 11:15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = {PhoneNumberValidator.class})
public @interface PhoneNumber {

    String message()

            default "{validate.phonenumber.illegal}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
