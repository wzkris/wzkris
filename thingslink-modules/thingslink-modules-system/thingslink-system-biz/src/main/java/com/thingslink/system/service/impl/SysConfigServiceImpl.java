package com.thingslink.system.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.common.core.constant.CommonConstants;
import com.thingslink.common.core.exception.BusinessException;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.redis.util.RedisUtil;
import com.thingslink.system.domain.SysConfig;
import com.thingslink.system.mapper.SysConfigMapper;
import com.thingslink.system.service.SysConfigService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 参数配置 服务层实现
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysConfigServiceImpl implements SysConfigService {

    private final SysConfigMapper sysConfigMapper;

    static class ConfigCache {
        static final String CONFIG_KEY_PREFIX = "sys_config";

        static String getConfigValueByKey(String configKey) {
            return RedisUtil.getMapValue(CONFIG_KEY_PREFIX, configKey);
        }

        static void setConfigValueByKey(String configKey, String configValue) {
            RedisUtil.setMapValue(CONFIG_KEY_PREFIX, configKey, configValue);
        }

        static void setConfig(Map<String, Object> map) {
            RedisUtil.setMap(CONFIG_KEY_PREFIX, map);
        }

        static void deleteByKey(String configKey) {
            RedisUtil.getMap(CONFIG_KEY_PREFIX).remove(configKey);
        }

        static void clearAll() {
            RedisUtil.delObj(CONFIG_KEY_PREFIX);
        }
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String getConfigValueByKey(String configKey) {
        String configValue = ConfigCache.getConfigValueByKey(configKey);
        if (StringUtil.isNotEmpty(configValue)) {
            return configValue;
        }
        SysConfig retConfig = sysConfigMapper.selectConfig(configKey);
        if (StringUtil.isNotNull(retConfig)) {
            ConfigCache.setConfigValueByKey(configKey, retConfig.getConfigValue());
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
            ConfigCache.setConfigValueByKey(config.getConfigKey(), config.getConfigValue());
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
        int row = sysConfigMapper.updateById(config);
        if (row > 0) {
            ConfigCache.setConfigValueByKey(config.getConfigKey(), config.getConfigValue());
        }
        return row;
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    @Override
    public void deleteConfigByIds(List<Long> configIds) {
        List<SysConfig> configs = sysConfigMapper.selectBatchIds(configIds);
        for (SysConfig config : configs) {
            if (StringUtil.equals(CommonConstants.YES, config.getConfigType())) {
                throw new BusinessException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
        }
        ConfigCache.clearAll();
        sysConfigMapper.deleteByIds(configIds);
        ConfigCache.clearAll();
    }

    /**
     * 项目启动时，初始化参数到缓存
     */
    @Override
    @PostConstruct
    public void loadingConfigCache() {
        List<SysConfig> configsList = sysConfigMapper.selectList(null);
        for (SysConfig config : configsList) {
            ConfigCache.setConfigValueByKey(config.getConfigKey(), config.getConfigValue());
        }
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache() {
        ConfigCache.clearAll();
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
                .ne(ObjUtil.isNotNull(config.getConfigId()), SysConfig::getConfigId, config.getConfigId());
        return sysConfigMapper.exists(lqw);
    }
}
