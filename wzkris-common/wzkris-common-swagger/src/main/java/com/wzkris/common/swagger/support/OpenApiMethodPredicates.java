package com.wzkris.common.swagger.support;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.reflect.Method;

/**
 * SpringDoc {@link org.springdoc.core.models.GroupedOpenApi} 用的方法级过滤条件。
 */
public final class OpenApiMethodPredicates {

    private OpenApiMethodPredicates() {}

    /**
     * 与项目习惯一致：{@code @Tag} 可标在 Controller 类或 Handler 方法上。
     */
    public static boolean hasTag(Method method) {
        if (method == null) {
            return false;
        }
        if (method.getAnnotation(Tag.class) != null) {
            return true;
        }
        return method.getDeclaringClass().getAnnotation(Tag.class) != null;
    }
}
