package com.wzkris.common.orm.utils;

import com.baomidou.mybatisplus.core.plugins.IgnoreStrategy;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.wzkris.common.security.utils.LoginUtil;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * 动态租户工具类
 *
 * <p>保持原始方法签名不变的安全优化版本</p>
 *
 * @author wzkris
 * @version 1.0.1
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DynamicTenantUtil {

    private static final ThreadLocal<Deque<Long>> LOCAL_DYNAMIC_TENANT = new ThreadLocal<>();

    private static final ThreadLocal<AtomicInteger> LOCAL_IGNORE = new ThreadLocal<>();

    /**
     * 获取动态租户, 不存在则返回自身租户ID, 未登录返回空
     */
    @Nullable
    public static Long get() {
        try {
            Deque<Long> stack = LOCAL_DYNAMIC_TENANT.get();
            return stack == null ? LoginUtil.getTenantId() : stack.peek();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置动态租户
     */
    public static void set(Long tenantId) {
        if (tenantId != null) {
            Deque<Long> stack = LOCAL_DYNAMIC_TENANT.get();
            if (stack == null) {
                stack = new ArrayDeque<>();
                LOCAL_DYNAMIC_TENANT.set(stack);
            }
            stack.push(tenantId);
        }
    }

    /**
     * 清理线程变量
     */
    public static void remove() {
        Deque<Long> stack = LOCAL_DYNAMIC_TENANT.get();
        if (stack != null && !stack.isEmpty()) {
            stack.pop();
            if (stack.isEmpty()) {
                LOCAL_DYNAMIC_TENANT.remove();
            }
        }
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
            InterceptorIgnoreHelper.handle(IgnoreStrategy.builder().tenantLine(true).build());
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
    public static <T> T ignoreWithThrowable(ThrowingSupplier<T, Throwable> supplier) throws Throwable {
        return ignoreIfWithThrowable(true, supplier);
    }

    /**
     * 条件忽略租户执行(可抛异常)
     */
    public static <T> T ignoreIfWithThrowable(boolean ignore, ThrowingSupplier<T, Throwable> supplier) throws Throwable {
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
     * 切换租户执行
     */
    public static void switcht(Long tenantId, Runnable runnable) {
        switchtIf(true, tenantId, runnable);
    }

    /**
     * 条件切换租户执行
     */
    public static void switchtIf(boolean swch, Long tenantId, Runnable runnable) {
        if (runnable == null) return;

        if (swch && tenantId != null) {
            try {
                set(tenantId);
                runnable.run();
            } finally {
                remove();
            }
        } else {
            runnable.run();
        }
    }

    /**
     * 切换租户执行带返回值
     */
    public static <T> T switcht(Long tenantId, Supplier<T> supplier) {
        return switchtIf(true, tenantId, supplier);
    }

    /**
     * 条件切换租户执行带返回值
     */
    public static <T> T switchtIf(boolean swch, Long tenantId, Supplier<T> supplier) {
        if (supplier == null) return null;

        if (swch && tenantId != null) {
            try {
                set(tenantId);
                return supplier.get();
            } finally {
                remove();
            }
        }
        return supplier.get();
    }

    /**
     * 切换租户执行(可抛异常)
     */
    public static <T> T switchtWithThrowable(Long tenantId, ThrowingSupplier<T, Throwable> supplier) throws Throwable {
        return switchtIfWithThrowable(true, tenantId, supplier);
    }

    /**
     * 条件切换租户执行(可抛异常)
     */
    public static <T> T switchtIfWithThrowable(boolean swch, Long tenantId, ThrowingSupplier<T, Throwable> supplier) throws Throwable {
        if (supplier == null) return null;

        if (swch && tenantId != null) {
            try {
                set(tenantId);
                return supplier.get();
            } finally {
                remove();
            }
        }
        return supplier.get();
    }

    /**
     * 清理所有线程变量
     */
    public static void clear() {
        LOCAL_DYNAMIC_TENANT.remove();
        LOCAL_IGNORE.remove();
    }

    /**
     * 可抛出异常的Supplier接口
     */
    @FunctionalInterface
    public interface ThrowingSupplier<T, E extends Throwable> {

        T get() throws E;
    }
}