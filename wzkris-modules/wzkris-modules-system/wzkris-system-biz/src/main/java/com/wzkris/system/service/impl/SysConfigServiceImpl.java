package com.wzkris.system.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.system.domain.SysConfig;
import com.wzkris.system.listener.event.RefreshConfigEvent;
import com.wzkris.system.mapper.SysConfigMapper;
import com.wzkris.system.service.SysConfigService;
import com.wzkris.system.utils.ConfigCacheUtil;
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
    public void insertConfig(SysConfig config) {
        if (configMapper.insert(config) > 0) {
            SpringUtil.getContext().publishEvent(new RefreshConfigEvent(config.getConfigKey(), config.getConfigValue()));
        }
    }

    @Override
    public void updateConfig(SysConfig config) {
        if (configMapper.updateById(config) > 0) {
            SpringUtil.getContext().publishEvent(new RefreshConfigEvent(config.getConfigKey(), config.getConfigValue()));
        }
    }

    @Override
    public void deleteByIds(List<Long> configIds) {
        if (configMapper.deleteByIds(configIds) > 0) {
            ConfigCacheUtil.clearAll();
            loadingConfigCache();
        }
    }

    @Override
    public boolean checkConfigKeyUnique(SysConfig config) {
        LambdaQueryWrapper<SysConfig> lqw = new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getConfigKey, config.getConfigKey())
                .ne(ObjUtil.isNotNull(config.getConfigId()), SysConfig::getConfigId, config.getConfigId());
        return configMapper.exists(lqw);
    }

}
