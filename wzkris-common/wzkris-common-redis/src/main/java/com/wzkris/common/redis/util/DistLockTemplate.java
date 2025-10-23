package com.wzkris.common.redis.util;

import com.wzkris.common.core.function.ThrowableSupplier;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式锁 工具类
 *
 * @author wzkris
 **/
@Slf4j
public abstract class DistLockTemplate {

    private static final String LOCK_KEY_PREFIX = "distlock:";

    public static boolean lockAndExecute(final String lockKey, Runnable runnable) {
        return lockAndExecute(lockKey, -1, -1, runnable);
    }

    public static boolean lockAndExecute(final String lockKey, final long waitLockTime, Runnable runnable) {
        return lockAndExecute(lockKey, waitLockTime, -1, runnable);
    }

    /**
     * @param lockKey      锁名
     * @param waitLockTime 获取锁等待时间，毫秒
     * @param lockTimeout  锁超时时间，毫秒
     * @param runnable     执行
     * @return 是否已执行
     */
    public static boolean lockAndExecute(final String lockKey, final long waitLockTime,
                                         final long lockTimeout, Runnable runnable) {
        try {
            RLock lock = RedisUtil.getLock(getLockKey(lockKey));
            boolean res = lock.tryLock(waitLockTime, lockTimeout, TimeUnit.MILLISECONDS);
            if (!res) {
                return false;
            }
            try {
                runnable.run();
                return true;
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            log.error("线程发生中断", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * @param lockKey  锁名
     * @param supplier 执行
     * @return 是否已执行
     */
    @Nullable
    public static <T> T lockAndExecute(final String lockKey, Supplier<T> supplier) {
        return lockAndExecute(lockKey, -1, -1, supplier);
    }

    @Nullable
    public static <T> T lockAndExecute(final String lockKey, final long waitLockTime, Supplier<T> supplier) {
        return lockAndExecute(lockKey, waitLockTime, -1, supplier);
    }

    /**
     * @param lockKey      锁名
     * @param waitLockTime 获取锁等待时间，毫秒
     * @param lockTimeout  锁超时时间，毫秒
     * @param supplier     执行
     * @return 是否已执行
     */
    @Nullable
    public static <T> T lockAndExecute(final String lockKey, final long waitLockTime,
                                       final long lockTimeout, Supplier<T> supplier) {
        try {
            RLock lock = RedisUtil.getLock(getLockKey(lockKey));
            boolean res = lock.tryLock(waitLockTime, lockTimeout, TimeUnit.MILLISECONDS);
            if (!res) {
                return null;
            }
            try {
                return supplier.get();
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            log.error("线程发生中断", e);
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public static <T> T lockAndExecute(final String lockKey, ThrowableSupplier<T, Throwable> throwableSupplier) throws Throwable {
        return lockAndExecute(lockKey, -1, -1, throwableSupplier);
    }

    public static <T> T lockAndExecute(final String lockKey, final long waitLockTime,
                                       ThrowableSupplier<T, Throwable> throwableSupplier) throws Throwable {
        return lockAndExecute(lockKey, waitLockTime, -1, throwableSupplier);
    }

    /**
     * @param lockKey           锁名
     * @param waitLockTime      获取锁等待时间，毫秒
     * @param lockTimeout       锁超时时间，毫秒
     * @param throwableSupplier 执行
     * @return 是否已执行
     */
    public static <T> T lockAndExecute(final String lockKey, final long waitLockTime,
                                       final long lockTimeout, ThrowableSupplier<T, Throwable> throwableSupplier) throws Throwable {
        try {
            RLock lock = RedisUtil.getLock(getLockKey(lockKey));
            boolean res = lock.tryLock(waitLockTime, lockTimeout, TimeUnit.MILLISECONDS);
            if (!res) {
                return null;
            }
            try {
                return throwableSupplier.get();
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            log.error("线程发生中断", e);
            Thread.currentThread().interrupt();
            return null;
        }
    }

    static String getLockKey(String lockKey) {
        return LOCK_KEY_PREFIX + lockKey;
    }

}
