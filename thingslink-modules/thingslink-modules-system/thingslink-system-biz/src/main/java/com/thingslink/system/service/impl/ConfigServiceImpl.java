package com.thingslink.system.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.thingslink.common.core.constant.CommonConstants;
import com.thingslink.common.core.exception.BusinessException;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.system.domain.Config;
import com.thingslink.system.mapper.ConfigMapper;
import com.thingslink.system.service.ConfigService;
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
public class ConfigServiceImpl implements ConfigService {
    private static final Cache<String, String> configCache = Caffeine.newBuilder()
            .maximumSize(500)
            .build();

    private final ConfigMapper configMapper;

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
        Config retConfig = configMapper.selectConfig(configKey);
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
    public List<Config> list(Config config) {
        LambdaQueryWrapper<Config> lqw = this.buildQueryWrapper(config);
        return configMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<Config> buildQueryWrapper(Config config) {
        return new LambdaQueryWrapper<Config>()
                .like(StringUtil.isNotNull(config.getConfigKey()), Config::getConfigKey, config.getConfigKey())
                .like(StringUtil.isNotNull(config.getConfigName()), Config::getConfigName, config.getConfigName())
                .like(StringUtil.isNotNull(config.getConfigType()), Config::getConfigType, config.getConfigType());
    }

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(Config config) {
        int row = configMapper.insert(config);
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
    public int updateConfig(Config config) {
        Config temp = configMapper.selectById(config.getConfigId());
        if (!StringUtil.equals(temp.getConfigKey(), config.getConfigKey())) {
            configCache.invalidate(temp.getConfigKey());
        }

        int row = configMapper.updateById(config);
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
            Config config = configMapper.selectById(configId);
            if (StringUtil.equals(CommonConstants.YES, config.getConfigType())) {
                throw new BusinessException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            configMapper.deleteById(configId);
            configCache.invalidate(config.getConfigKey());
        }
    }

    /**
     * 项目启动时，初始化参数到缓存
     */
    @Override
    @PostConstruct
    public void loadingConfigCache() {
        List<Config> configsList = configMapper.selectList(null);
        for (Config config : configsList) {
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
    public boolean checkConfigKeyUnique(Config config) {
        LambdaQueryWrapper<Config> lqw = new LambdaQueryWrapper<Config>()
                .eq(Config::getConfigKey, config.getConfigKey())
                .ne(Config::getConfigId, config.getConfigId());
        return configMapper.exists(lqw);
    }
}
