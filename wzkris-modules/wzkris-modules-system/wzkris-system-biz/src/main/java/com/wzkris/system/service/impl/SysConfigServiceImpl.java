package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.system.domain.SysConfig;
import com.wzkris.system.mapper.SysConfigMapper;
import com.wzkris.system.service.SysConfigService;
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
public class SysConfigServiceImpl implements SysConfigService {

    private static final String DICT_KEY = "sys_config";

    private final SysConfigMapper configMapper;

    private RMap<String, String> cache() {
        return RedisUtil.getRMap(DICT_KEY);
    }

    @PostConstruct
    @Override
    public void loadingConfigCache() {
        RMap<String, String> rMap = cache();
        Map<String, String> map = configMapper.selectList(null).stream()
                .collect(Collectors.toMap(SysConfig::getConfigKey, SysConfig::getConfigValue));
        rMap.clear();
        rMap.putAll(map);
    }

    @Override
    public boolean insertConfig(SysConfig config) {
        boolean success = configMapper.insert(config) > 0;
        if (success) {
            cache().put(config.getConfigKey(), config.getConfigValue());
        }
        return success;
    }

    @Override
    public boolean updateConfig(SysConfig config) {
        boolean success = configMapper.updateById(config) > 0;
        if (success) {
            cache().put(config.getConfigKey(), config.getConfigValue());
        }
        return success;
    }

    @Override
    public boolean deleteById(Long configId) {
        SysConfig config = configMapper.selectById(configId);
        boolean success = configMapper.deleteById(configId) > 0;
        if (success) {
            cache().remove(config.getConfigKey());
        }
        return success;
    }

    @Override
    public boolean checkUsedByConfigKey(@Nullable Long configId, @Nonnull String configKey) {
        LambdaQueryWrapper<SysConfig> lqw = new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getConfigKey, configKey)
                .ne(Objects.nonNull(configId), SysConfig::getConfigId, configId);
        return configMapper.exists(lqw);
    }

}
