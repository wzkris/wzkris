package com.wzkris.common.loadbalancer.core;

/**
 * @author : wzkris
 * @date : 2024/09/10 10:43
 * @description : 版本号上下文
 */
public class VersionContext {
    private static final ThreadLocal<String> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public static String getVersion() {
        return CONTEXT_THREAD_LOCAL.get();
    }

    public static void clear() {
        CONTEXT_THREAD_LOCAL.remove();
    }
}
