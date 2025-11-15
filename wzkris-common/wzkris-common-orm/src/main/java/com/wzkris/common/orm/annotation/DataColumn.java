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

    /**
     * 参数来源，支持 SpEL 表达式，用于指定从哪个字段或方法获取权限范围值
     * 
     * <p>示例：
     * <ul>
     *   <li>{@code "au.getDeptScopes()"} - 通过 AdminUtil Bean 调用方法（推荐）</li>
     *   <li>{@code "@au.getDeptScopes()"} - 通过 Bean 引用调用方法</li>
     *   <li>{@code "admin.deptScopes"} - 通过 admin 变量访问属性</li>
     * </ul>
     */
    String source() default "au.getDeptScopes()";

}
