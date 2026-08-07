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

    /**
     * 收录服务间远程调用接口：声明类名以 {@code RemoteController} 结尾，
     * 或位于 *.remote.controller 包下。
     */
    public static boolean isRemoteController(Method method) {
        if (method == null) {
            return false;
        }
        Class<?> declaringClass = method.getDeclaringClass();
        return declaringClass.getSimpleName().endsWith("RemoteController")
                || declaringClass.getPackageName().contains(".remote.controller");
    }

    /**
     * 业务接口：带 {@code @Tag} 且不属于 remote controller。remote controller 即使标了
     * {@code @Tag} 也排除，保证默认分组与 remote 分组互斥。
     */
    public static boolean isBusiness(Method method) {
        return hasTag(method) && !isRemoteController(method);
    }
}
