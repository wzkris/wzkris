package com.wzkris.system.utils;

import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.system.domain.GlobalDictData;

import java.util.List;

/**
 * 字典缓存工具类
 *
 * @author wzkris
 */
public class DictCacheUtil {

    public static final String DICT_KEY = "global_dict";

    /**
     * 设置字典缓存
     *
     * @param key      参数键
     * @param dictData 字典数据列表
     */
    public static void setDictCache(String key, List<GlobalDictData> dictData) {
        RedisUtil.setMapValue(DICT_KEY, key, dictData);
    }

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static <T> T getDictCache(String key) {
        return RedisUtil.getMapValue(DICT_KEY, key);
    }

    /**
     * 删除指定字典缓存
     *
     * @param key 字典键
     */
    public static void removeDictCache(String key) {
        RedisUtil.delMapValue(DICT_KEY, key);
    }

    /**
     * 清空字典缓存
     */
    public static void clearAll() {
        RedisUtil.delObj(DICT_KEY);
    }
}
