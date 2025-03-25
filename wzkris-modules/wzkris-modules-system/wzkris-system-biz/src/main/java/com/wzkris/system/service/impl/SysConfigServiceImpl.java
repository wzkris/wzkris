package com.wzkris.system.service.impl;

import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.system.domain.SysConfig;
import com.wzkris.system.listener.event.RefreshConfigEvent;
import com.wzkris.system.mapper.SysConfigMapper;
import com.wzkris.system.service.SysConfigService;
import com.wzkris.system.utils.ConfigCacheUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 参数配置 服务层实现
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysConfigServiceImpl implements SysConfigService {

    private final SysConfigMapper configMapper;

    @Override
    public void loadingConfigCache() {
        Map<String, String> map = StreamUtil.of(configMapper.selectList(null))
                .collect(Collectors.toMap(SysConfig::getConfigKey, SysConfig::getConfigValue));
        ConfigCacheUtil.set(map);
    }

    @Override
    public boolean insertConfig(SysConfig config) {
        boolean success = configMapper.insert(config) > 0;
        if (success) {
            SpringUtil.getContext().publishEvent(new RefreshConfigEvent(config.getConfigKey(), config.getConfigValue()));
        }
        return success;
    }

    @Override
    public boolean updateConfig(SysConfig config) {
        boolean success = configMapper.updateById(config) > 0;
        if (success) {
            SpringUtil.getContext().publishEvent(new RefreshConfigEvent(config.getConfigKey(), config.getConfigValue()));
        }
        return success;
    }

    @Override
    public boolean deleteById(Long configId) {
        SysConfig config = configMapper.selectById(configId);
        boolean success = configMapper.deleteById(configId) > 0;
        if (success) {
            ConfigCacheUtil.remove(config.getConfigKey());
        }
        return success;
    }

    @Override
    public boolean checkUsedByConfigKey(@Nullable Long configId, @Nonnull String configKey) {
        LambdaQueryWrapper<SysConfig> lqw = new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getConfigKey, configKey)
                .ne(ObjUtil.isNotNull(configId), SysConfig::getConfigId, configId);
        return configMapper.exists(lqw);
    }

}
