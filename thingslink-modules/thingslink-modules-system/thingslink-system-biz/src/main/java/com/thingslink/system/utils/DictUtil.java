package com.thingslink.system.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.thingslink.system.domain.DictData;

import java.util.List;

/**
 * 字典工具类
 *
 * @author wzkris
 */
public class DictUtil {
    // 字典缓存
    private static final Cache<String, List<DictData>> dictCache = Caffeine.newBuilder()
            .maximumSize(500)
            .build();

    /**
     * 设置字典缓存
     *
     * @param key       参数键
     * @param dictDatas 字典数据列表
     */
    public static void setDictCache(String key, List<DictData> dictDatas) {
        dictCache.put(key, dictDatas);
    }

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static List<DictData> getDictCache(String key) {
        return dictCache.getIfPresent(key);
    }

    /**
     * 删除指定字典缓存
     *
     * @param key 字典键
     */
    public static void removeDictCache(String key) {
        dictCache.invalidate(key);
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache() {
        dictCache.cleanUp();
    }

}
