package com.wzkris.system.utils;

import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.system.domain.SysDictData;

import java.util.List;

/**
 * 字典工具类
 *
 * @author wzkris
 */
public class DictUtil {

    private static final String dict_key = "sys_dict";

    /**
     * 设置字典缓存
     *
     * @param key         参数键
     * @param sysDictData 字典数据列表
     */
    public static void setDictCache(String key, List<SysDictData> sysDictData) {
        RedisUtil.setMapValue(dict_key, key, sysDictData);
    }

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    @SuppressWarnings("unchecked")
    public static <T> T getDictCache(String key) {
        return (T) RedisUtil.getMapValue(dict_key, key);
    }

    /**
     * 删除指定字典缓存
     *
     * @param key 字典键
     */
    public static void removeDictCache(String key) {
        RedisUtil.delMapValue(dict_key, key);
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache() {
        RedisUtil.delObj(dict_key);
    }
}
