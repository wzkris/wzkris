package com.wzkris.system.utils;

import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.system.domain.GlobalDictData;
import org.redisson.api.RMap;

import java.util.List;
import java.util.Map;

/**
 * 字典缓存工具类
 *
 * @author wzkris
 */
public class DictCacheUtil {

    public static final String DICT_KEY = "global_dict";

    public static void put(String key, List<GlobalDictData> dictData) {
        RedisUtil.getRMap(DICT_KEY).put(key, dictData);
    }

    public static <T> T get(String key) {
        RMap<String, T> rMap = RedisUtil.getRMap(DICT_KEY);
        return rMap.get(key);
    }

    public static <T> void set(Map<String, T> map) {
        RedisUtil.getRMap(DICT_KEY).putAll(map);
    }

    public static void remove(String key) {
        RedisUtil.getRMap(DICT_KEY).remove(key);
    }

    public static void clearAll() {
        RedisUtil.getRMap(DICT_KEY).delete();
    }
}
