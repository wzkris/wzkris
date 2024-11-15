package com.wzkris.common.orm.utils;

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

    private static final ThreadLocal<Long> LOCAL_DYNAMIC_TENANT = new ThreadLocal<>();

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
        if (tenantId != null) {// 避免get到null值无法判断是不存在还是set null
            LOCAL_DYNAMIC_TENANT.set(tenantId);
        }
    }

    /**
     * 清理线程变量
     */
    public static void remove() {
        LOCAL_DYNAMIC_TENANT.remove();
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

    public static void ignore(Runnable runnable) {
        ignoreIf(true, runnable);
    }

    /**
     * 在忽略租户中执行
     *
     * @param igno     是否忽略
     * @param runnable 执行方法
     */
    public static void ignoreIf(boolean igno, Runnable runnable) {
        if (igno) {
            try {
                enableIgnore();
                runnable.run();
            }
            finally {
                disableIgnore();
            }
        }
        else {
            runnable.run();
        }
    }

    public static <T> T ignore(Supplier<T> supplier) {
        return ignoreIf(true, supplier);
    }

    /**
     * 在忽略租户中执行
     *
     * @param igno     是否忽略
     * @param supplier 执行方法
     */
    public static <T> T ignoreIf(boolean igno, Supplier<T> supplier) {
        if (igno) {
            try {
                enableIgnore();
                return supplier.get();
            }
            finally {
                disableIgnore();
            }
        }
        else {
            return supplier.get();
        }
    }

    public static <T> T ignoreWithThrowable(ThrowingSupplier<T, Throwable> supplier) throws Throwable {
        return ignoreIfWithThrowable(true, supplier);
    }

    /**
     * 在忽略租户中执行, 并且会抛出异常
     *
     * @param igno     是否忽略
     * @param supplier 执行方法
     */
    public static <T> T ignoreIfWithThrowable(boolean igno, ThrowingSupplier<T, Throwable> supplier) throws Throwable {
        if (igno) {
            try {
                enableIgnore();
                return supplier.get();
            }
            finally {
                disableIgnore();
            }
        }
        else {
            return supplier.get();
        }
    }

    public static void switcht(Long tenantId, Runnable runnable) {
        switchtIf(true, tenantId, runnable);
    }

    /**
     * 切换租户并执行代码块
     *
     * @param swch     执行条件
     * @param tenantId 租户ID
     * @param runnable 表达式
     */
    public static void switchtIf(boolean swch, Long tenantId, Runnable runnable) {
        if (swch) {
            try {
                set(tenantId);
                runnable.run();
            }
            finally {
                remove();
            }
        }
        else {
            runnable.run();
        }
    }

    /**
     * 有返回值的切换租户并执行代码块
     */
    public static <T> T switcht(Long tenantId, Supplier<T> supplier) {
        return switchtIf(true, tenantId, supplier);
    }

    /**
     * 有返回值的切换租户并执行代码块
     */
    public static <T> T switchtIf(boolean swch, Long tenantId, Supplier<T> supplier) {
        if (swch) {
            try {
                set(tenantId);
                return supplier.get();
            }
            finally {
                remove();
            }
        }
        else {
            return supplier.get();
        }
    }

    public static <T> T switchtWithThrowable(Long tenantId, ThrowingSupplier<T, Throwable> supplier) throws Throwable {
        return switchtIfWithThrowable(true, tenantId, supplier);
    }

    /**
     * 有返回值的切换租户并执行代码块, 并且会抛出异常
     */
    public static <T> T switchtIfWithThrowable(boolean swch, Long tenantId, ThrowingSupplier<T, Throwable> supplier) throws Throwable {
        if (swch) {
            try {
                set(tenantId);
                return supplier.get();
            }
            finally {
                remove();
            }
        }
        else {
            return supplier.get();
        }
    }

    /**
     * 自定义抛异常的Supplier
     */
    @FunctionalInterface
    public interface ThrowingSupplier<T, E extends Throwable> {
        T get() throws E;
    }
}