package com.wzkris.common.openfeign.interceptor;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.openfeign.annotation.FeignIgnoreInterceptor;
import feign.RequestTemplate;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 拦截器注解缓存
 *
 * @author wzkris
 * @date 2025/07/08
 */
public abstract class InterceptorCacheUtil {

    static final ConcurrentHashMap<String, Boolean> cache = new ConcurrentHashMap<>();

    /**
     * 检查当前请求是否需要跳过拦截器（按就近原则：方法注解 > 类注解）
     *
     * @param template Feign请求模板
     * @return true表示需要跳过拦截器
     */
    public static boolean checkSkipInterceptor(RequestTemplate template) {
        Class<?> targetType = template.feignTarget().type();
        Method method = template.methodMetadata().method();
        String cacheKey = targetType.getName() + StringUtil.DOT + method.getName();

        // 优先从缓存读取
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        // 1. 先检查方法上的注解
        boolean shouldSkip = Optional.of(method)
                .map(m -> m.isAnnotationPresent(FeignIgnoreInterceptor.class))
                .orElse(false);

        // 2. 方法上没有则检查类上的注解
        if (!shouldSkip) {
            shouldSkip = Optional.of(targetType)
                    .map(c -> c.isAnnotationPresent(FeignIgnoreInterceptor.class))
                    .orElse(false);
        }

        // 存入缓存
        cache.put(cacheKey, shouldSkip);
        return shouldSkip;
    }

}
