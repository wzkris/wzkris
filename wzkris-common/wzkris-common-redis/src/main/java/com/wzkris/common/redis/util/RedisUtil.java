package com.wzkris.common.redis.util;

import com.wzkris.common.core.utils.SpringUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.redisson.api.*;

import java.time.Duration;
import java.util.List;

/**
 * spring redis 工具类
 *
 * @author wzkris
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisUtil {

    private static final RedissonClient redissonclient = SpringUtil.getFactory().getBean(RedissonClient.class);

    public static RedissonClient getClient() {
        return redissonclient;
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public static <T> T getObj(final String key) {
        return (T) redissonclient.getBucket(key).get();
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key     缓存的键值
     * @param value   缓存的值
     * @param timeout 时间，单位秒
     */
    public static <T> void setObj(final String key, final T value, final long timeout) {
        setObj(key, value, Duration.ofSeconds(timeout));
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param duration 时间
     */
    public static <T> void setObj(final String key, final T value, final Duration duration) {
        redissonclient.getBucket(key).set(value, duration);
    }

    /**
     * 删除单个对象
     *
     * @return 是否删除
     */
    public static boolean delObj(final String key) {
        return redissonclient.getBucket(key).delete();
    }

    /**
     * 批量删除key
     *
     * @return 删除个数
     */
    public static long delObjs(final List<String> keys) {
        return redissonclient.getKeys().delete(keys.toArray(new String[0]));
    }

    /**
     * 获取桶
     *
     * @param key 缓存的键值
     */
    public static <T> RBucket<T> getBucket(final String key) {
        return redissonclient.getBucket(key);
    }

    /**
     * 获得缓存Map
     *
     * @param key 缓存的键值
     * @return map对象
     */
    public static <T> RMap<String, T> getRMap(final String key) {
        return redissonclient.getMap(key);
    }

    /**
     * 获得具有单个key过期时间的缓存Map
     *
     * @param key 缓存的键值
     * @return map对象
     */
    public static <T> RMapCache<String, T> getRMapCache(final String key) {
        return redissonclient.getMapCache(key);
    }

    /**
     * 获取过期时间
     *
     * @param key Redis键
     * @return 有效时间 秒
     */
    public static long getExpire(final String key) {
        long time = redissonclient.getBucket(key).remainTimeToLive();
        return time <= 0 ? time : time / 1000;
    }

    public static boolean expire(final String key, final long timeout) {
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

    public static boolean expireIfSet(final String key, final long timeout) {
        return expireIfSet(key, Duration.ofSeconds(timeout));
    }

    /**
     * 值存在才设置有效时间，否则不做任何操作
     *
     * @param key      Redis键
     * @param duration 过期时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expireIfSet(final String key, final Duration duration) {
        return redissonclient.getBucket(key).expireIfSet(duration);
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static boolean hasKey(final String key) {
        return redissonclient.getBucket(key).isExists();
    }

    /**
     * @param key 键
     * @return 返回个数
     */
    public static long countKey(final List<String> key) {
        return redissonclient.getKeys().countExists(key.toArray(new String[0]));
    }

    /**
     * 模糊匹配key
     */
    public static List<String> keysByPattern(final String keyPattern, final int count) {
        return redissonclient.getKeys().getKeysStreamByPattern(keyPattern, count).toList();
    }

    /**
     * 获取非公平锁
     */
    public static RLock getLock(final String key) {
        return redissonclient.getLock(key);
    }

}
