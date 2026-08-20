package com.wzkris.common.orm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限
 * <p>
 * 标注在 Mapper 方法或 Service 方法上，声明数据权限规则。
 * 支持多规则组合（AND 关系）。
 *
 * @author wzkris
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 数据权限规则配置，支持多个规则（AND 关系）
     *
     * @return 规则配置数组
     */
    DataPermission[] value() default {};

}
