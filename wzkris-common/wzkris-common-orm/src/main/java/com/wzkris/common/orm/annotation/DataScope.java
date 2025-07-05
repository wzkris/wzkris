package com.wzkris.common.orm.annotation;

import java.lang.annotation.*;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 数据权限
 * @date : 2023/12/11 8:59
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 指定权限列
     */
    DataColumn[] value();

}
