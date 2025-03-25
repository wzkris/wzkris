package com.wzkris.system.utils;

import com.wzkris.common.redis.util.RedisUtil;
import org.redisson.api.RMap;

import java.util.Map;

/**
 * 系统配置缓存工具类
 *
 * @author wzkris
 */
public class ConfigCacheUtil {

    public static final String CACHE_KEY = "sys_config";

    public static <T> T get(String key) {
        RMap<String, T> rMap = RedisUtil.getRMap(CACHE_KEY);
        return rMap.get(key);
    }

    public static void put(String key, String value) {
        RedisUtil.getRMap(CACHE_KEY).put(key, value);
    }

    public static void remove(String key) {
        RedisUtil.getRMap(CACHE_KEY).remove(key);
    }

    public static <T> void set(Map<String, T> map) {
        RedisUtil.getRMap(CACHE_KEY).putAll(map);
    }

    public static void clearAll() {
        RedisUtil.getRMap(CACHE_KEY).delete();
    }
}
