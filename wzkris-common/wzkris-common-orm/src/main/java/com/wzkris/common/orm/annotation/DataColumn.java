package com.wzkris.common.orm.annotation;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 数据权限 列
 * @date : 2025/07/04 10:00
 */
public @interface DataColumn {

    /**
     * 表别名
     */
    String alias() default "";

    /**
     * 指定列
     */
    String column();

}
