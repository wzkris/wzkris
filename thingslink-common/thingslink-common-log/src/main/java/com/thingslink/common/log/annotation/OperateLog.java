package com.thingslink.common.log.annotation;

import com.thingslink.common.log.enums.OperateType;

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
     * 模块
     */
    String title() default "";

    /**
     * 操作类型
     */
    OperateType operateType() default OperateType.OTHER;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default false;

    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default false;
}
