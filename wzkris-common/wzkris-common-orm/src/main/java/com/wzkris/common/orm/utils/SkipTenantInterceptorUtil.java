package com.wzkris.common.orm.utils;

import com.baomidou.mybatisplus.core.plugins.IgnoreStrategy;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.wzkris.common.core.function.ThrowableSupplier;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class SkipTenantInterceptorUtil {

    private static final ThreadLocal<AtomicInteger> LOCAL_IGNORE = new ThreadLocal<>();

    /**
     * 忽略租户执行
     */
    public static void ignore(Runnable runnable) {
        ignoreIf(true, runnable);
    }

    /**
     * 条件忽略租户执行
     */
    public static void ignoreIf(boolean ignore, Runnable runnable) {
        if (runnable == null) return;

        if (ignore) {
            try {
                enableIgnore();
                runnable.run();
            } finally {
                disableIgnore();
            }
        } else {
            runnable.run();
        }
    }

    /**
     * 忽略租户执行带返回值
     */
    public static <T> T ignore(Supplier<T> supplier) {
        return ignoreIf(true, supplier);
    }

    /**
     * 条件忽略租户执行带返回值
     */
    public static <T> T ignoreIf(boolean ignore, Supplier<T> supplier) {
        if (supplier == null) return null;

        if (ignore) {
            try {
                enableIgnore();
                return supplier.get();
            } finally {
                disableIgnore();
            }
        }
        return supplier.get();
    }

    /**
     * 忽略租户执行(可抛异常)
     */
    public static <T> T ignoreWithThrowable(ThrowableSupplier<T, Throwable> supplier) throws Throwable {
        return ignoreIfWithThrowable(true, supplier);
    }

    /**
     * 条件忽略租户执行(可抛异常)
     */
    public static <T> T ignoreIfWithThrowable(boolean ignore, ThrowableSupplier<T, Throwable> supplier)
            throws Throwable {
        if (supplier == null) return null;

        if (ignore) {
            try {
                enableIgnore();
                return supplier.get();
            } finally {
                disableIgnore();
            }
        }
        return supplier.get();
    }

    /**
     * 开启忽略租户
     */
    public static void enableIgnore() {
        AtomicInteger counter = LOCAL_IGNORE.get();
        if (counter == null) {
            counter = new AtomicInteger(0);
            LOCAL_IGNORE.set(counter);
        }
        if (counter.getAndIncrement() == 0) {
            InterceptorIgnoreHelper.handle(
                    IgnoreStrategy.builder().tenantLine(true).build());
        }
    }

    /**
     * 关闭忽略租户
     */
    public static void disableIgnore() {
        AtomicInteger counter = LOCAL_IGNORE.get();
        if (counter != null && counter.decrementAndGet() <= 0) {
            LOCAL_IGNORE.remove();
            InterceptorIgnoreHelper.clearIgnoreStrategy();
        }
    }

}
