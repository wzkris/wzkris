package com.wzkris.common.web.annotation;

import java.lang.annotation.*;

/**
 * 排除controller打印
 *
 * @author wzkris
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExControllerStat {

}
