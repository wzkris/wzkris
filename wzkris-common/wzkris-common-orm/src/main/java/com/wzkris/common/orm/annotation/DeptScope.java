package com.wzkris.common.orm.annotation;

import java.lang.annotation.*;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 部门权限
 * @date : 2023/12/11 8:59
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DeptScope {

    /**
     * 表的别名
     */
    String tableAlias() default "";

    /**
     * 字段的别名
     */
    String columnAlias() default "dept_id";

}
