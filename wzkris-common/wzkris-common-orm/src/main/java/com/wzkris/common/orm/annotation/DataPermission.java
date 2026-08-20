package com.wzkris.common.orm.annotation;

import com.wzkris.common.orm.rule.DataPermissionType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限规则配置，作为 {@link DataScope} 的元素使用
 * <p>
 * 通过 type 指定规则类型，对应 {@link com.wzkris.common.orm.rule.DataPermissionRule#getType()}
 *
 * @author wzkris
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

    /**
     * 规则类型，默认 {@link DataPermissionType#DEPT}
     *
     * @return 规则类型
     */
    DataPermissionType type() default DataPermissionType.DEPT;

    /**
     * 表别名（多表 JOIN 时用于匹配当前解析的表，为空则应用于所有表）
     *
     * @return 别名
     */
    String alias() default "";

    /**
     * 列名
     *
     * @return 列名
     */
    String column();

}
