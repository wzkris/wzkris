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

    public static String getConfigValueByKey(String configKey) {
        return RedisUtil.getMapValue(CONFIG_KEY_PREFIX, configKey);
    }

    public static void setKey(String configKey, String configValue) {
        RedisUtil.setMapValue(CONFIG_KEY_PREFIX, configKey, configValue);
    }

    public static void setConfig(Map<String, Object> map) {
        RedisUtil.setMap(CONFIG_KEY_PREFIX, map);
    }

    public static void deleteByKey(String configKey) {
        RedisUtil.getMap(CONFIG_KEY_PREFIX).remove(configKey);
    }

    public static void clearAll() {
        RedisUtil.delObj(CONFIG_KEY_PREFIX);
    }
}
