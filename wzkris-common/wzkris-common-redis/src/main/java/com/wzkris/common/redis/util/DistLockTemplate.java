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
public final class DistLockTemplate {

    private DistLockTemplate() {
    }

    private static final String LOCK_KEY_PREFIX = "distlock:";

    public static boolean lockAndExecute(final String lockKey, Runnable runnable) {
        return lockAndExecute(lockKey, -1, -1, runnable);
    }

    public static boolean lockAndExecute(final String lockKey, final long waitLockTime, Runnable runnable) {
        return lockAndExecute(lockKey, waitLockTime, -1, runnable);
    }

    /**
     * @param lockKey      锁名
     * @param waitLockTime 获取锁等待时间，毫秒。小于0表示不等待，立即返回
     * @param lockTimeout  锁超时时间，毫秒。小于0表示不设置超时，需要手动释放
     * @param runnable     执行
     * @return 是否已执行
     */
    public static boolean lockAndExecute(final String lockKey, final long waitLockTime,
                                         final long lockTimeout, Runnable runnable) {
        final String distLockKey = getLockKey(lockKey);

        final RLock lock = RedisUtil.getLock(distLockKey);
        boolean locked = false;
        try {
            locked = lock.tryLock(waitLockTime, lockTimeout, TimeUnit.MILLISECONDS);
            if (locked) {
                runnable.run();
            }
        } catch (Exception e) {
            log.error("获取分布式锁或执行任务时发生异常, lockKey: {}", distLockKey, e);
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
        return locked;
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
     * @param waitLockTime 获取锁等待时间，毫秒。小于0表示不等待，立即返回
     * @param lockTimeout  锁超时时间，毫秒。小于0表示不设置超时，需要手动释放
     * @param supplier     执行
     * @return 执行结果，获取锁失败返回null
     */
    @Nullable
    public static <T> T lockAndExecute(
            final String lockKey, final long waitLockTime,
            final long lockTimeout, Supplier<T> supplier) {

        final String distLockKey = getLockKey(lockKey);

        final RLock lock = RedisUtil.getLock(distLockKey);
        boolean locked = false;
        try {
            locked = lock.tryLock(waitLockTime, lockTimeout, TimeUnit.MILLISECONDS);
            if (locked) {
                return supplier.get();
            }
        } catch (Exception e) {
            log.error("获取分布式锁或执行任务时发生异常, lockKey: {}", distLockKey, e);
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
        return null;
    }

    @Nullable
    public static <T> T lockAndExecute(final String lockKey, ThrowableSupplier<T, Throwable> throwableSupplier) throws Throwable {
        return lockAndExecute(lockKey, -1, -1, throwableSupplier);
    }

    @Nullable
    public static <T> T lockAndExecute(final String lockKey, final long waitLockTime,
                                       ThrowableSupplier<T, Throwable> throwableSupplier) throws Throwable {
        return lockAndExecute(lockKey, waitLockTime, -1, throwableSupplier);
    }

    /**
     * @param lockKey           锁名
     * @param waitLockTime      获取锁等待时间，毫秒。小于0表示不等待，立即返回
     * @param lockTimeout       锁超时时间，毫秒。小于0表示不设置超时，需要手动释放
     * @param throwableSupplier 执行，可能抛出异常
     * @return 执行结果，获取锁失败返回null
     * @throws Throwable 业务代码抛出的异常
     */
    @Nullable
    public static <T> T lockAndExecute(
            final String lockKey, final long waitLockTime,
            final long lockTimeout, ThrowableSupplier<T, Throwable> throwableSupplier) throws Throwable {

        final String distLockKey = getLockKey(lockKey);

        final RLock lock = RedisUtil.getLock(distLockKey);
        boolean locked = false;
        try {
            locked = lock.tryLock(waitLockTime, lockTimeout, TimeUnit.MILLISECONDS);
            if (locked) {
                return throwableSupplier.get();
            }
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
        return null;
    }

    private static String getLockKey(String lockKey) {
        return LOCK_KEY_PREFIX + lockKey;
    }

}
