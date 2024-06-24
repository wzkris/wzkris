package com.thingslink.common.orm.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.baomidou.mybatisplus.core.plugins.IgnoreStrategy;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 动态租户工具
 * @date : 2023/12/11 10:54
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DynamicTenantUtil {

    private static final ThreadLocal<Long> LOCAL_DYNAMIC_TENANT = new TransmittableThreadLocal<>();

    @FunctionalInterface
    public interface ThrowingSupplier<T, E extends Throwable> {
        T get() throws E;
    }

    /**
     * 开启忽略租户(开启后需手动调用 {@link #disableIgnore()} 关闭)
     * 开启后则不会再走租户拦截器
     */
    public static void enableIgnore() {
        InterceptorIgnoreHelper.handle(IgnoreStrategy.builder().tenantLine(true).build());
    }

    /**
     * 关闭忽略租户
     */
    public static void disableIgnore() {
        InterceptorIgnoreHelper.clearIgnoreStrategy();
    }

    /**
     * 在忽略租户中执行
     *
     * @param runnable 处理执行方法
     */
    public static void ignore(Runnable runnable) {
        try {
            enableIgnore();

            runnable.run();
        }
        finally {
            disableIgnore();
        }
    }

    /**
     * 在忽略租户中执行
     *
     * @param supplier 处理执行方法
     */
    public static <T> T ignore(Supplier<T> supplier) {
        try {
            enableIgnore();

            return supplier.get();
        }
        finally {
            disableIgnore();
        }
    }

    /**
     * 在忽略租户中执行, 并且会抛出异常
     */
    public static <T> T ignoreWithThrowable(ThrowingSupplier<T, Throwable> supplier) throws Throwable {
        try {
            enableIgnore();

            return supplier.get();
        }
        finally {
            disableIgnore();
        }
    }

    /**
     * 获取动态租户
     * 当前线程内生效
     */
    public static Long get() {
        return LOCAL_DYNAMIC_TENANT.get();
    }

    /**
     * 设置动态租户，若设置了值则一定会走租户拦截
     * 当前线程内生效
     */
    public static void set(Long tenantId) {
        LOCAL_DYNAMIC_TENANT.set(tenantId);
    }

    /**
     * 切换租户并执行代码块
     */
    public static void switcht(Long tenantId, Runnable runnable) {
        try {
            LOCAL_DYNAMIC_TENANT.set(tenantId);

            runnable.run();
        }
        finally {
            LOCAL_DYNAMIC_TENANT.remove();
        }
    }

    /**
     * 有返回值的切换租户并执行代码块
     */
    public static <T> T switcht(Long tenantId, Supplier<T> supplier) {
        try {
            LOCAL_DYNAMIC_TENANT.set(tenantId);

            return supplier.get();
        }
        finally {
            LOCAL_DYNAMIC_TENANT.remove();
        }
    }

    /**
     * 有返回值的切换租户并执行代码块, 并且会抛出异常
     */
    public static <T> T switchtWithThrowable(Long tenantId, ThrowingSupplier<T, Throwable> supplier) throws Throwable {
        try {
            LOCAL_DYNAMIC_TENANT.set(tenantId);

            return supplier.get();
        }
        finally {
            LOCAL_DYNAMIC_TENANT.remove();
        }
    }
}