package com.wzkris.common.redis.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.*;
import org.redisson.api.options.KeysScanOptions;
import org.redisson.codec.TypedJsonJacksonCodec;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * spring redis 工具类
 *
 * @author wzkris
 **/
public final class RedisUtil {

    /**
     * Codec缓存，避免重复创建TypedJsonJacksonCodec实例
     */
    private static final ConcurrentMap<String, TypedJsonJacksonCodec> CODEC_CACHE = new ConcurrentHashMap<>(64);

    private static RedissonClient client;

    private static ObjectMapper objectMapper;

    private RedisUtil(RedissonClient client, ObjectMapper objectMapper) {
        RedisUtil.client = client;
        RedisUtil.objectMapper = objectMapper;
    }

    /**
     * 获取或创建TypedJsonJacksonCodec实例（单类型）
     */
    private static TypedJsonJacksonCodec getCodec(Class<?> clazz) {
        String key = clazz.getName();
        return CODEC_CACHE.computeIfAbsent(key, k -> new TypedJsonJacksonCodec(clazz, objectMapper));
    }

    /**
     * 获取或创建TypedJsonJacksonCodec实例（键值类型）
     */
    private static TypedJsonJacksonCodec getCodec(Class<?> keyClass, Class<?> valueClass) {
        String key = keyClass.getName() + ":" + valueClass.getName();
        return CODEC_CACHE.computeIfAbsent(key, k -> new TypedJsonJacksonCodec(keyClass, valueClass));
    }

    // ==================== 基础对象操作 ====================

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据，如果键不存在返回null
     */
    public static <T> T getObj(final String key) {
        if (key == null || key.trim().isEmpty()) {
            return null;
        }
        Object o = getBucket(key).get();

        return o == null ? null : (T) o;
    }

    /**
     * 获得缓存的基本对象（带类型）
     *
     * @param key   缓存键值
     * @param clazz 类型
     * @return 缓存键值对应的数据，如果键不存在返回null
     */
    public static <T> T getObj(final String key, Class<T> clazz) {
        if (key == null || key.trim().isEmpty()) {
            return null;
        }
        Object o = getBucket(key, clazz).get();

        return o == null ? null : (T) o;
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key     缓存的键值
     * @param value   缓存的值
     * @param seconds 时间，单位秒
     */
    public static <T> void setObj(final String key, final T value, final long seconds) {
        setObj(key, value, Duration.ofSeconds(seconds));
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param duration 时间
     */
    public static <T> void setObj(final String key, final T value, final Duration duration) {
        if (key == null || key.trim().isEmpty()) {
            return;
        }
        if (duration == null || duration.isNegative() || duration.isZero()) {
            return;
        }
        getBucket(key, value.getClass()).set(value, duration);
    }

    /**
     * 缓存基本的对象，永不过期
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public static <T> void setObj(final String key, final T value) {
        setObj(key, value, -1);
    }

    /**
     * 如果键不存在则设置值
     *
     * @param key   键名
     * @param value 值
     * @return true=设置成功，false=键已存在
     */
    public static <T> boolean setObjIfAbsent(final String key, final T value) {
        if (key == null || key.trim().isEmpty()) {
            return false;
        }
        return getBucket(key, value.getClass()).setIfAbsent(value);
    }

    /**
     * 如果键不存在则设置值（带过期时间）
     *
     * @param key     键名
     * @param value   值
     * @param seconds 过期时间（秒）
     * @return true=设置成功，false=键已存在
     */
    public static <T> boolean setObjIfAbsent(final String key, final T value, final long seconds) {
        if (key == null || key.trim().isEmpty()) {
            return false;
        }
        return getBucket(key, value.getClass()).setIfAbsent(value, Duration.ofSeconds(seconds));
    }

    /**
     * 如果键存在则设置值
     *
     * @param key   键名
     * @param value 值
     * @return true=设置成功，false=键不存在
     */
    public static <T> boolean setObjIfPresent(final String key, final T value) {
        if (key == null || key.trim().isEmpty()) {
            return false;
        }
        return getBucket(key, value.getClass()).setIfExists(value);
    }

    /**
     * 如果键存在则设置值（带过期时间）
     *
     * @param key     键名
     * @param value   值
     * @param seconds 过期时间（秒）
     * @return true=设置成功，false=键不存在
     */
    public static <T> boolean setObjIfPresent(final String key, final T value, final long seconds) {
        if (key == null || key.trim().isEmpty()) {
            return false;
        }
        return getBucket(key, value.getClass()).setIfExists(value, Duration.ofSeconds(seconds));
    }

    // ==================== 键操作 ====================

    /**
     * 删除单个对象
     *
     * @param key 缓存的键值
     * @return 是否删除成功
     */
    public static boolean delObj(final String key) {
        if (key == null || key.trim().isEmpty()) {
            return false;
        }
        return getBucket(key).delete();
    }

    /**
     * 批量删除key
     *
     * @param keys 要删除的键列表
     * @return 删除个数
     */
    public static long delObjs(final List<String> keys) {
        return delObjs(keys.toArray(new String[0]));
    }

    /**
     * 批量删除key（数组形式）
     *
     * @param keys 要删除的键数组
     * @return 删除个数
     */
    public static long delObjs(final String... keys) {
        if (keys == null || keys.length == 0) {
            return 0;
        }
        return delObjs(List.of(keys));
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static boolean exist(final String key) {
        if (key == null || key.trim().isEmpty()) {
            return false;
        }
        return getBucket(key).isExists();
    }

    /**
     * 批量检查键是否存在
     *
     * @param keys 键列表
     * @return 存在的键数量
     */
    public static long countExists(final List<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return 0;
        }
        return client.getKeys().countExists(keys.toArray(new String[0]));
    }

    /**
     * 批量检查键是否存在（数组形式）
     *
     * @param keys 键数组
     * @return 存在的键数量
     */
    public static long countExists(final String... keys) {
        if (keys == null || keys.length == 0) {
            return 0;
        }
        return countExists(List.of(keys));
    }

    /**
     * 模糊匹配key
     *
     * @param keyPattern 键模式
     * @param count      返回数量限制
     * @return 匹配的键列表
     */
    public static List<String> keysByPattern(final String keyPattern, final int count) {
        if (keyPattern == null || keyPattern.trim().isEmpty()) {
            return new ArrayList<>();
        }
        int limit = count <= 0 ? 100 : count; // 默认限制100个
        List<String> keys = new ArrayList<>();
        client.getKeys()
                .getKeys(KeysScanOptions.defaults().pattern(keyPattern).limit(limit))
                .forEach(keys::add);
        return keys;
    }

    /**
     * 模糊匹配key（无数量限制）
     *
     * @param keyPattern 键模式
     * @return 匹配的键列表
     */
    public static List<String> keysByPattern(final String keyPattern) {
        return keysByPattern(keyPattern, 1000); // 默认最多1000个
    }

    /**
     * 获取所有键
     *
     * @return 所有键的集合
     */
    public static Set<String> getAllKeys() {
        Set<String> keys = new java.util.HashSet<>();
        client.getKeys().getKeys().forEach(keys::add);
        return keys;
    }

    /**
     * 重命名键
     *
     * @param oldKey 旧键名
     * @param newKey 新键名
     */
    public static void rename(final String oldKey, final String newKey) {
        client.getKeys().rename(oldKey, newKey);
    }

    // ==================== 过期时间操作 ====================

    /**
     * 设置有效时间
     *
     * @param key      Redis键
     * @param duration 过期时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final Duration duration) {
        if (key == null || key.trim().isEmpty()) {
            return false;
        }
        if (duration == null || duration.isNegative() || duration.isZero()) {
            return false;
        }
        return getBucket(key).expire(duration);
    }

    /**
     * 设置有效时间（秒）
     *
     * @param key     Redis键
     * @param seconds 过期时间（秒）
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final long seconds) {
        return expire(key, Duration.ofSeconds(seconds));
    }

    /**
     * 值存在才设置有效时间，否则不做任何操作
     *
     * @param key      Redis键
     * @param duration 过期时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expireIfSet(final String key, final Duration duration) {
        if (key == null || key.trim().isEmpty()) {
            return false;
        }
        if (duration == null || duration.isNegative() || duration.isZero()) {
            return false;
        }
        return getBucket(key).expireIfSet(duration);
    }

    /**
     * 值存在才设置有效时间，否则不做任何操作（秒）
     *
     * @param key     Redis键
     * @param seconds 过期时间（秒）
     * @return true=设置成功；false=设置失败
     */
    public static boolean expireIfSet(final String key, final long seconds) {
        return expireIfSet(key, Duration.ofSeconds(seconds));
    }

    // ==================== 哈希表(Map)操作 ====================

    /**
     * 获得缓存Map
     *
     * @param key 缓存的键值
     * @return map对象
     */
    public static <T> RMap<String, T> getRMap(final String key) {
        return client.getMap(key);
    }

    /**
     * 获得缓存Map（带类型化编解码器）
     *
     * @param key   缓存的键值
     * @param clazz 类型
     * @return map对象
     */
    public static <T> RMap<String, T> getRMap(final String key, final Class<T> clazz) {
        return client.getMap(key, getCodec(clazz));
    }

    /**
     * 获得缓存Map（带键值类型化编解码器）
     *
     * @param key           缓存的键值
     * @param mapKeyClass   键类型
     * @param mapValueClass 值类型
     * @return map对象
     */
    public static <T> RMap<String, T> getRMap(final String key, final Class<?> mapKeyClass, final Class<?> mapValueClass) {
        return client.getMap(key, getCodec(mapKeyClass, mapValueClass));
    }

    /**
     * 获得具有单个key过期时间的缓存Map
     *
     * @param key 缓存的键值
     * @return map对象
     */
    public static <T> RMapCache<String, T> getRMapCache(final String key) {
        return client.getMapCache(key);
    }

    /**
     * 获得具有单个key过期时间的缓存Map（带类型化编解码器）
     *
     * @param key   缓存的键值
     * @param clazz 类型
     * @return map对象
     */
    public static <T> RMapCache<String, T> getRMapCache(final String key, final Class<T> clazz) {
        return client.getMapCache(key, getCodec(clazz));
    }

    /**
     * 获得具有单个key过期时间的缓存Map（带键值类型化编解码器）
     *
     * @param key           缓存的键值
     * @param mapKeyClass   键类型
     * @param mapValueClass 值类型
     * @return map对象
     */
    public static <K, V> RMapCache<K, V> getRMapCache(final String key, final Class<?> mapKeyClass, final Class<?> mapValueClass) {
        return client.getMapCache(key, getCodec(mapKeyClass, mapValueClass));
    }

    // ==================== 列表(List)操作 ====================

    /**
     * 获取列表
     *
     * @param key 缓存的键值
     * @return 列表对象
     */
    public static <T> RList<T> getRList(final String key) {
        return client.getList(key);
    }

    /**
     * 获取列表（带类型化编解码器）
     *
     * @param key   缓存的键值
     * @param clazz 类型
     * @return 列表对象
     */
    public static <T> RList<T> getRList(final String key, final Class<T> clazz) {
        return client.getList(key, getCodec(clazz));
    }

    /**
     * 向列表头部添加元素
     *
     * @param key   缓存的键值
     * @param value 要添加的值
     * @return 添加后列表的长度
     */
    public static <T> int lpush(final String key, final T value) {
        RList<T> list = client.getList(key, getCodec(value.getClass()));
        list.add(0, value);
        return list.size();
    }

    /**
     * 向列表尾部添加元素
     *
     * @param key   缓存的键值
     * @param value 要添加的值
     * @return 添加后列表的长度
     */
    public static <T> int rpush(final String key, final T value) {
        RList<T> list = client.getList(key, getCodec(value.getClass()));
        list.add(value);
        return list.size();
    }

    /**
     * 从列表头部弹出元素
     *
     * @param key 缓存的键值
     * @return 弹出的元素
     */
    public static <T> T lpop(final String key) {
        RList<T> list = client.getList(key);
        if (list.isEmpty()) {
            return null;
        }
        return list.remove(0);
    }

    /**
     * 从列表尾部弹出元素
     *
     * @param key 缓存的键值
     * @return 弹出的元素
     */
    public static <T> T rpop(final String key) {
        RList<T> list = client.getList(key);
        if (list.isEmpty()) {
            return null;
        }
        return list.remove(list.size() - 1);
    }

    /**
     * 获取列表长度
     *
     * @param key 缓存的键值
     * @return 列表长度
     */
    public static int llen(final String key) {
        return client.getList(key).size();
    }

    /**
     * 获取列表指定范围的元素
     *
     * @param key   缓存的键值
     * @param start 开始位置
     * @param end   结束位置
     * @return 指定范围的元素列表
     */
    public static <T> List<T> lrange(final String key, final int start, final int end) {
        return (List<T>) client.getList(key).range(start, end);
    }

    // ==================== 集合(Set)操作 ====================

    /**
     * 获取集合
     *
     * @param key 缓存的键值
     * @return 集合对象
     */
    public static <T> RSet<T> getRSet(final String key) {
        return client.getSet(key);
    }

    /**
     * 获取集合（带类型化编解码器）
     *
     * @param key   缓存的键值
     * @param clazz 类型
     * @return 集合对象
     */
    public static <T> RSet<T> getRSet(final String key, final Class<T> clazz) {
        return client.getSet(key, getCodec(clazz));
    }

    /**
     * 向集合添加元素
     *
     * @param key   缓存的键值
     * @param value 要添加的值
     * @return 是否添加成功
     */
    public static <T> boolean sadd(final String key, final T value) {
        return client.getSet(key, getCodec(value.getClass())).add(value);
    }

    /**
     * 从集合移除元素
     *
     * @param key   缓存的键值
     * @param value 要移除的值
     * @return 是否移除成功
     */
    public static <T> boolean srem(final String key, final T value) {
        return client.getSet(key, getCodec(value.getClass())).remove(value);
    }

    /**
     * 判断元素是否在集合中
     *
     * @param key   缓存的键值
     * @param value 要检查的值
     * @return 是否存在
     */
    public static <T> boolean sismember(final String key, final T value) {
        return client.getSet(key, getCodec(value.getClass())).contains(value);
    }

    /**
     * 获取集合大小
     *
     * @param key 缓存的键值
     * @return 集合大小
     */
    public static int scard(final String key) {
        return client.getSet(key).size();
    }

    /**
     * 获取集合所有成员
     *
     * @param key 缓存的键值
     * @return 所有成员
     */
    public static <T> Set<T> smembers(final String key) {
        return (Set<T>) client.getSet(key).readAll();
    }

    // ==================== 有序集合(SortedSet)操作 ====================

    /**
     * 获取有序集合
     *
     * @param key 缓存的键值
     * @return 有序集合对象
     */
    public static <T> RSortedSet<T> getRSortedSet(final String key) {
        return client.getSortedSet(key);
    }

    /**
     * 获取有序集合（带类型化编解码器）
     *
     * @param key   缓存的键值
     * @param clazz 类型
     * @return 有序集合对象
     */
    public static <T> RSortedSet<T> getRSortedSet(final String key, final Class<T> clazz) {
        return client.getSortedSet(key, getCodec(clazz));
    }

    public static <T> RScoredSortedSet<T> getScoredSortedSet(String zsetKey) {
        return client.getScoredSortedSet(zsetKey);
    }

    public static <T> RScoredSortedSet<T> getScoredSortedSet(String zsetKey, final Class<T> clazz) {
        return client.getScoredSortedSet(zsetKey, getCodec(clazz));
    }

    /**
     * 从有序集合移除元素
     *
     * @param key   缓存的键值
     * @param value 要移除的值
     * @return 是否移除成功
     */
    public static <T> boolean zrem(final String key, final T value) {
        return client.getSortedSet(key, getCodec(value.getClass())).remove(value);
    }

    /**
     * 获取有序集合大小
     *
     * @param key 缓存的键值
     * @return 有序集合大小
     */
    public static int zcard(final String key) {
        return client.getSortedSet(key).size();
    }

    // ==================== 发布订阅(Pub/Sub)操作 ====================

    /**
     * 发布消息
     *
     * @param channel 频道
     * @param message 消息
     * @return 接收到消息的客户端数量
     */
    public static long publish(final String channel, final Object message) {
        return client.getTopic(channel).publish(message);
    }

    /**
     * 订阅频道
     *
     * @param channel 频道
     * @return 主题对象
     */
    public static <T> RTopic getRTopic(final String channel) {
        return client.getTopic(channel);
    }

    // ==================== 分布式锁操作 ====================

    /**
     * 获取分布式锁
     *
     * @param key 锁的键值
     * @return 锁对象
     */
    public static RLock getLock(final String key) {
        return client.getLock(key);
    }

    /**
     * 获取公平锁
     *
     * @param key 锁的键值
     * @return 公平锁对象
     */
    public static RLock getFairLock(final String key) {
        return client.getFairLock(key);
    }

    /**
     * 获取读写锁
     *
     * @param key 锁的键值
     * @return 读写锁对象
     */
    public static RReadWriteLock getReadWriteLock(final String key) {
        return client.getReadWriteLock(key);
    }

    /**
     * 获取信号量
     *
     * @param key 信号量的键值
     * @return 信号量对象
     */
    public static RSemaphore getSemaphore(final String key) {
        return client.getSemaphore(key);
    }

    /**
     * 获取闭锁
     *
     * @param key 闭锁的键值
     * @return 闭锁对象
     */
    public static RCountDownLatch getCountDownLatch(final String key) {
        return client.getCountDownLatch(key);
    }

    // ==================== 原子计数器操作 ====================

    /**
     * 获取原子长整型
     *
     * @param key 缓存的键值
     * @return 原子长整型对象
     */
    public static RAtomicLong getAtomicLong(final String key) {
        return client.getAtomicLong(key);
    }

    /**
     * 原子递增
     *
     * @param key 缓存的键值
     * @return 递增后的值
     */
    public static long incr(final String key) {
        return client.getAtomicLong(key).incrementAndGet();
    }

    /**
     * 原子递减
     *
     * @param key 缓存的键值
     * @return 递减后的值
     */
    public static long decr(final String key) {
        return client.getAtomicLong(key).decrementAndGet();
    }

    /**
     * 原子递增指定值
     *
     * @param key   缓存的键值
     * @param delta 递增值
     * @return 递增后的值
     */
    public static long incrBy(final String key, final long delta) {
        return client.getAtomicLong(key).addAndGet(delta);
    }

    /**
     * 原子递减指定值
     *
     * @param key   缓存的键值
     * @param delta 递减值
     * @return 递减后的值
     */
    public static long decrBy(final String key, final long delta) {
        return client.getAtomicLong(key).addAndGet(-delta);
    }

    // ==================== 核心工具方法 ====================

    /**
     * 获取桶
     *
     * @param key 缓存的键值
     */
    public static <T> RBucket<T> getBucket(final String key) {
        return client.getBucket(key);
    }

    /**
     * 获取桶（带类型化编解码器）
     *
     * @param key   缓存的键值
     * @param clazz 类型
     */
    public static <T> RBucket<T> getBucket(final String key, final Class<?> clazz) {
        return client.getBucket(key, getCodec(clazz));
    }

    /**
     * 获取脚本
     */
    public static RScript getScript() {
        return client.getScript();
    }

    /**
     * 创建批量操作
     */
    public static RBatch createBatch() {
        return client.createBatch();
    }

    /**
     * 创建批量操作（有序）
     */
    public static RBatch createBatch(BatchOptions options) {
        return client.createBatch(options);
    }

}