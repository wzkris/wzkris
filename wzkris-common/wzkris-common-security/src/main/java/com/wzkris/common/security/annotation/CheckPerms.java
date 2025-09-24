package com.wzkris.common.security.annotation;

import com.wzkris.common.security.annotation.enums.CheckMode;
import com.wzkris.common.security.enums.AuthType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限认证校验：必须具有指定权限才能进入该方法。
 *
 * <p> 可标注在方法、类上（效果等同于标注在此类的所有方法上）
 *
 * @author wzkris
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
public @interface CheckPerms {

    /**
     * 多账号体系登录标识
     *
     * @return /
     */
    AuthType checkType();

    /**
     * 需要校验的权限码 [ 数组 ]
     *
     * @return /
     */
    String[] value() default {};

    /**
     * 验证模式：AND | OR，默认AND
     *
     * @return /
     */
    CheckMode mode() default CheckMode.AND;

}
