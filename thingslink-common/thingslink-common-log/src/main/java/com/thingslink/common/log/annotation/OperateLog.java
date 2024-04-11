package com.thingslink.common.log.annotation;

import com.thingslink.common.log.enums.BusinessType;
import com.thingslink.common.log.enums.OperatorType;

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
     * 功能
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    OperatorType operatorType() default OperatorType.USER;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default false;

    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default false;
}
