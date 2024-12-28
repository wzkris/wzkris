package com.wzkris.system.service.impl;

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

import java.util.List;

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
        List<SysConfig> configsList = configMapper.selectList(null);
        for (SysConfig config : configsList) {
            ConfigCacheUtil.setKey(config.getConfigKey(), config.getConfigValue());
        }
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
    public void deleteByIds(List<Long> configIds) {
        if (configMapper.deleteByIds(configIds) > 0) {
            ConfigCacheUtil.clearAll();
            loadingConfigCache();
        }
    }

    @Override
    public boolean checkUsedByConfigKey(@Nullable Long configId, @Nonnull String configKey) {
        LambdaQueryWrapper<SysConfig> lqw = new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getConfigKey, configKey)
                .ne(ObjUtil.isNotNull(configId), SysConfig::getConfigId, configId);
        return configMapper.exists(lqw);
    }

}
