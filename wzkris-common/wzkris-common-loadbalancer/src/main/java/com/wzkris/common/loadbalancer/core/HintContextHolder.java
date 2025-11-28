package com.wzkris.common.loadbalancer.core;

/**
 * 标签上下文
 *
 * @author : wzkris
 */
public class HintContextHolder {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static String get() {
        return CONTEXT.get();
    }

    public static void set(String hint) {
        CONTEXT.set(hint);
    }

    public static void clear() {
        CONTEXT.remove();
    }

}
