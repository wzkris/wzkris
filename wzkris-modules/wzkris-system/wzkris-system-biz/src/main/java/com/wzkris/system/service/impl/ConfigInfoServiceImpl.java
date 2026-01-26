package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.system.domain.ConfigInfoDO;
import com.wzkris.system.mapper.ConfigInfoMapper;
import com.wzkris.system.service.ConfigInfoService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 参数配置 服务层实现
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class ConfigInfoServiceImpl implements ConfigInfoService, SmartInitializingSingleton {

    private static final String DICT_KEY = "system-config";

    private final ConfigInfoMapper configInfoMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void afterSingletonsInstantiated() {
        loadingConfigCache();
    }

    @Override
    public void loadingConfigCache() {
        Map<String, String> map = configInfoMapper.selectList(null).stream()
                .collect(Collectors.toMap(ConfigInfoDO::getConfigKey, ConfigInfoDO::getConfigValue));
        redisTemplate.delete(DICT_KEY);
        if (!map.isEmpty()) {
            redisTemplate.opsForHash().putAll(DICT_KEY, (Map) map);
        }
    }

    @Override
    public String getValueByKey(String configkey) {
        Object value = redisTemplate.opsForHash().get(DICT_KEY, configkey);
        if (value instanceof String && StringUtil.isNotBlank((String) value)) {
            return (String) value;
        }
        value = configInfoMapper.selectValueByKey(configkey);
        if (value != null) {
            redisTemplate.opsForHash().put(DICT_KEY, configkey, value);
        }
        return value != null ? value.toString() : null;
    }

    @Override
    public boolean insertConfig(ConfigInfoDO config) {
        boolean success = configInfoMapper.insert(config) > 0;
        if (success) {
            redisTemplate.opsForHash().put(DICT_KEY, config.getConfigKey(), config.getConfigValue());
        }
        return success;
    }

    @Override
    public boolean updateConfig(ConfigInfoDO config) {
        boolean success = configInfoMapper.updateById(config) > 0;
        if (success) {
            redisTemplate.opsForHash().put(DICT_KEY, config.getConfigKey(), config.getConfigValue());
        }
        return success;
    }

    @Override
    public boolean deleteById(Long configId) {
        ConfigInfoDO config = configInfoMapper.selectById(configId);
        boolean success = configInfoMapper.deleteById(configId) > 0;
        if (success) {
            redisTemplate.opsForHash().delete(DICT_KEY, config.getConfigKey());
        }
        return success;
    }

    @Override
    public boolean checkUsedByConfigKey(@Nullable Long configId, @Nonnull String configKey) {
        LambdaQueryWrapper<ConfigInfoDO> lqw = new LambdaQueryWrapper<ConfigInfoDO>()
                .eq(ConfigInfoDO::getConfigKey, configKey)
                .ne(Objects.nonNull(configId), ConfigInfoDO::getConfigId, configId);
        return configInfoMapper.exists(lqw);
    }

}
