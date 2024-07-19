package com.wzkris.common.redis.util;

import com.wzkris.common.core.utils.SpringUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * spring redis 工具类
 *
 * @author wzkris
 **/
@SuppressWarnings(value = {"unchecked"})
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
    public static long delObj(final List<String> keys) {
        return redissonclient.getKeys().delete(keys.toArray(new String[0]));
    }

    /**
     * 获得缓存的Map
     *
     * @param key 缓存的键值
     * @return map对象
     */
    public static <T> Map<String, T> getMap(final String key) {
        RMap<String, T> rMap = redissonclient.getMap(key);
        return rMap.getAll(rMap.keySet());
    }

    /**
     * 获得缓存Map的key列表
     *
     * @param key 缓存的键值
     * @return key列表
     */
    public static <T> Set<String> getMapKeySet(final String key) {
        RMap<String, T> rMap = redissonclient.getMap(key);
        return rMap.keySet();
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public static <T> T getMapValue(final String key, final String hKey) {
        RMap<String, T> rMap = redissonclient.getMap(key);
        return rMap.get(hKey);
    }

    /**
     * 删除Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public static boolean delMapValue(final String key, final String hKey) {
        RMap<String, Object> rMap = redissonclient.getMap(key);
        return rMap.remove(hKey) != null;
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public static <K, V> Map<K, V> getMapMultipleValue(final String key, final Set<K> hKeys) {
        RMap<K, V> rMap = redissonclient.getMap(key);
        return rMap.getAll(hKeys);
    }

    /**
     * 缓存HASH
     *
     * @param key Redis键
     * @param map HashMap
     */
    public static <T> void setMap(final String key, final Map<String, Object> map) {
        redissonclient.getMap(key).putAll(map);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public static <T> void setMapValue(final String key, final String hKey, final T value) {
        redissonclient.getMap(key).put(hKey, value);
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间 秒
     */
    public static long getTimeToLive(final String key) {
        long time = redissonclient.getBucket(key).remainTimeToLive();
        return time <= 0 ? time : time / 1000;
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 时间，单位秒
     * @return true=设置成功；false=设置失败
     */
    public static boolean setTimeToLive(final String key, final long timeout) {
        return setTimeToLive(key, Duration.ofSeconds(timeout));
    }

    /**
     * 设置有效时间
     *
     * @param key      Redis键
     * @param duration 过期时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean setTimeToLive(final String key, final Duration duration) {
        return redissonclient.getBucket(key).expire(duration);
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
     * @return 返回个数
     */
    public static long countKey(List<String> key) {
        return redissonclient.getKeys().countExists(key.toArray(new String[0]));
    }

    /**
     * 模糊匹配key
     */
    public static List<String> keysByPattern(String keyPattern, int count) {
        return redissonclient.getKeys().getKeysStreamByPattern(keyPattern, count).toList();
    }

    /**
     * 获取非公平锁
     */
    public static RLock getLock(String key) {
        return redissonclient.getLock(key);
    }

}
