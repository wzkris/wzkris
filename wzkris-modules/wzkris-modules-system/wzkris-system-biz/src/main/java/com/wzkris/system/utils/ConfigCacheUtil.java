package com.wzkris.system.utils;

import com.wzkris.common.redis.util.RedisUtil;

import java.util.Map;


/**
 * 系统配置缓存工具类
 *
 * @author wzkris
 */
public class ConfigCacheUtil {

    public static final String CONFIG_KEY_PREFIX = "sys_config";

    public static <T> T getConfigValueByKey(String configKey) {
        return (T) RedisUtil.getRMap(CONFIG_KEY_PREFIX).get(configKey);
    }

    public static void setKey(String configKey, String configValue) {
        RedisUtil.getRMap(CONFIG_KEY_PREFIX).put(configKey, configValue);
    }

    public static void setConfig(Map<String, Object> map) {
        RedisUtil.getRMap(CONFIG_KEY_PREFIX).putAll(map);
    }

    public static void deleteByKey(String configKey) {
        RedisUtil.getRMap(CONFIG_KEY_PREFIX).remove(configKey);
    }

    public static void clearAll() {
        RedisUtil.getRMap(CONFIG_KEY_PREFIX).delete();
    }
}
