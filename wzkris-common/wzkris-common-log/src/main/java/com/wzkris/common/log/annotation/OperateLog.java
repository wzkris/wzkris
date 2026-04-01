package com.wzkris.common.log.annotation;

import com.wzkris.common.log.enums.OperateTypeEnum;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 *
 * @author wzkris
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {

    /**
     * 标题
     */
    String title() default "";

    /**
     * 子标题
     */
    String subTitle() default "";

    /**
     * 操作类型
     */
    OperateTypeEnum type() default OperateTypeEnum.OTHER;

    /**
     * 请求参数排除
     */
    String[] excludeRequestParam() default {};

}
