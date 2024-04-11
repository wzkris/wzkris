package com.thingslink.system.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.thingslink.common.core.constant.CommonConstants;
import com.thingslink.common.core.exception.BusinessException;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.system.domain.SysConfig;
import com.thingslink.system.mapper.SysConfigMapper;
import com.thingslink.system.service.SysConfigService;
import jakarta.annotation.PostConstruct;
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
    private static final Cache<String, String> configCache = Caffeine.newBuilder()
            .maximumSize(500)
            .build();

    private final SysConfigMapper sysConfigMapper;

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String getConfigValueByKey(String configKey) {
        String configValue = Convert.toStr(configCache.getIfPresent(configKey));
        if (StringUtil.isNotEmpty(configValue)) {
            return configValue;
        }
        SysConfig retConfig = sysConfigMapper.selectConfig(configKey);
        if (StringUtil.isNotNull(retConfig)) {
            configCache.put(configKey, retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtil.EMPTY;
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<SysConfig> list(SysConfig config) {
        LambdaQueryWrapper<SysConfig> lqw = this.buildQueryWrapper(config);
        return sysConfigMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<SysConfig> buildQueryWrapper(SysConfig config) {
        return new LambdaQueryWrapper<SysConfig>()
                .like(StringUtil.isNotNull(config.getConfigKey()), SysConfig::getConfigKey, config.getConfigKey())
                .like(StringUtil.isNotNull(config.getConfigName()), SysConfig::getConfigName, config.getConfigName())
                .like(StringUtil.isNotNull(config.getConfigType()), SysConfig::getConfigType, config.getConfigType());
    }

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(SysConfig config) {
        int row = sysConfigMapper.insert(config);
        if (row > 0) {
            configCache.put(config.getConfigKey(), config.getConfigValue());
        }
        return row;
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(SysConfig config) {
        SysConfig temp = sysConfigMapper.selectById(config.getConfigId());
        if (!StringUtil.equals(temp.getConfigKey(), config.getConfigKey())) {
            configCache.invalidate(temp.getConfigKey());
        }

        int row = sysConfigMapper.updateById(config);
        if (row > 0) {
            configCache.put(config.getConfigKey(), config.getConfigValue());
        }
        return row;
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    @Override
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            SysConfig config = sysConfigMapper.selectById(configId);
            if (StringUtil.equals(CommonConstants.YES, config.getConfigType())) {
                throw new BusinessException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            sysConfigMapper.deleteById(configId);
            configCache.invalidate(config.getConfigKey());
        }
    }

    /**
     * 项目启动时，初始化参数到缓存
     */
    @Override
    @PostConstruct
    public void loadingConfigCache() {
        List<SysConfig> configsList = sysConfigMapper.selectList(null);
        for (SysConfig config : configsList) {
            configCache.put(config.getConfigKey(), config.getConfigValue());
        }
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache() {
        configCache.cleanUp();
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public boolean checkConfigKeyUnique(SysConfig config) {
        LambdaQueryWrapper<SysConfig> lqw = new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getConfigKey, config.getConfigKey())
                .ne(SysConfig::getConfigId, config.getConfigId());
        return sysConfigMapper.exists(lqw);
    }
}
