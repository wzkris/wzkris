package com.wzkris.common.security.annotation;

import com.wzkris.auth.rmi.enums.AuthenticatedType;
import com.wzkris.common.security.annotation.enums.CheckMode;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@CheckPerms(checkType = AuthenticatedType.SYSTEM_USER) // 将原注解作为元注解
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
public @interface CheckSystemPerms {

    @AliasFor(annotation = CheckPerms.class, attribute = "value")
    String[] value() default {};

    @AliasFor(annotation = CheckPerms.class, attribute = "mode")
    CheckMode mode() default CheckMode.AND;

}
