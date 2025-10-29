package com.wzkris.gateway.security.annotation;

import com.wzkris.common.core.enums.AuthType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 网关权限认证注解
 * <p>用于WebFlux环境下的轻量级权限验证
 *
 * @author wzkris
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequireAuth {

    /**
     * 认证类型（用户类型）
     *
     * @return 认证类型
     */
    AuthType authType();

    /**
     * 需要的权限标识（支持多个）
     * <p>为空表示只验证登录，不验证权限
     *
     * @return 权限标识数组
     */
    String[] permissions() default {};

    /**
     * 权限验证模式
     *
     * @return 验证模式
     */
    CheckMode mode() default CheckMode.AND;

    /**
     * 权限验证模式
     */
    enum CheckMode {
        /**
         * 必须拥有所有权限
         */
        AND,
        /**
         * 拥有任一权限即可
         */
        OR
    }

}

