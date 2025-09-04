package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.system.domain.ConfigInfoDO;
import com.wzkris.system.mapper.ConfigInfoMapper;
import com.wzkris.system.service.ConfigInfoService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
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
public class ConfigInfoServiceImpl implements ConfigInfoService {

    private static final String DICT_KEY = "config_info";

    private final ConfigInfoMapper configInfoMapper;

    private RMap<String, String> cache() {
        return RedisUtil.getRMap(DICT_KEY);
    }

    @PostConstruct
    @Override
    public void loadingConfigCache() {
        RMap<String, String> rMap = cache();
        Map<String, String> map = configInfoMapper.selectList(null).stream()
                .collect(Collectors.toMap(ConfigInfoDO::getConfigKey, ConfigInfoDO::getConfigValue));
        rMap.clear();
        rMap.putAll(map);
    }

    @Override
    public String getValueByKey(String configkey) {
        String value = cache().get(configkey);
        if (StringUtil.isNotBlank(value)) return value;
        value = configInfoMapper.selectValueByKey(configkey);
        cache().put(configkey, value);
        return value;
    }

    @Override
    public boolean insertConfig(ConfigInfoDO config) {
        boolean success = configInfoMapper.insert(config) > 0;
        if (success) {
            cache().put(config.getConfigKey(), config.getConfigValue());
        }
        return success;
    }

    @Override
    public boolean updateConfig(ConfigInfoDO config) {
        boolean success = configInfoMapper.updateById(config) > 0;
        if (success) {
            cache().put(config.getConfigKey(), config.getConfigValue());
        }
        return success;
    }

    @Override
    public boolean deleteById(Long configId) {
        ConfigInfoDO config = configInfoMapper.selectById(configId);
        boolean success = configInfoMapper.deleteById(configId) > 0;
        if (success) {
            cache().remove(config.getConfigKey());
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
