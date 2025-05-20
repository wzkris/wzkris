package com.wzkris.common.log.annotation;

import java.lang.annotation.*;

/**
 * 自定义废弃API注解
 *
 * @author wzkris
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DeprecatedAPI {

}
