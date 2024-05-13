package com.thingslink.common.security.annotation;

import org.springframework.security.access.annotation.Secured;

import java.lang.annotation.*;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 内部访问注解
 * @date : 2023/4/18 14:44
 */
//@Secured("SCOPE_inner")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface InnerAuth {

}
