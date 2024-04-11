package com.thingslink.common.redis.util;

import com.thingslink.common.core.utils.SpringUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * spring redis 工具类
 *
 * @author wzkris
 **/
@SuppressWarnings(value = {"unchecked"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisUtil {

    private static final RedissonClient redissonclient = SpringUtil.getBean(RedissonClient.class);

    public static RedissonClient getClient() {
        return redissonclient;
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key     缓存的键值
     * @param value   缓存的值
     * @param timeout 时间，单位秒
     */
    public static <T> void setCacheObject(final String key, final T value, final long timeout) {
        redissonclient.getBucket(key).set(value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public static <T> void setCacheObject(final String key,
                                          final T value,
                                          final long timeout,
                                          final TimeUnit timeUnit) {
        redissonclient.getBucket(key).set(value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expireBySecond(final String key, final long timeout) {
        return expire(key, Duration.ofSeconds(timeout));
    }

    /**
     * 设置有效时间
     *
     * @param key      Redis键
     * @param duration 过期时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final Duration duration) {
        return redissonclient.getBucket(key).expire(duration);
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间 秒
     */
    public static long getExpire(final String key) {
        long time = redissonclient.getBucket(key).remainTimeToLive();
        return time <= 0 ? time : time / 1000;
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static boolean hasKey(String key) {
        return redissonclient.getBucket(key).isExists();
    }

    /**
     * @param key 键
     * @return 返回🗡个数
     */
    public static long countKey(String key) {
        return redissonclient.getKeys().countExists(key);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public static <T> T getCacheObject(final String key) {
        return (T) redissonclient.getBucket(key).get();
    }

    /**
     * 删除单个对象
     */
    public static boolean deleteObject(final String key) {
        return redissonclient.getBucket(key).delete();
    }

    /**
     * 批量删除key
     */
    public static void deleteObject(final String... keys) {
        redissonclient.getKeys().delete(keys);
    }

    /**
     * 模糊匹配key
     */
    public static Iterator<String> keysByPattern(String keyPattern) {
        RKeys rKeys = redissonclient.getKeys();
        return rKeys.getKeysByPattern(keyPattern).iterator();
    }
}
