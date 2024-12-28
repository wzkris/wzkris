package com.wzkris.system.service;

import com.wzkris.system.domain.SysConfig;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * 参数配置 服务层
 *
 * @author wzkris
 */
public interface SysConfigService {

    /**
     * 加载参数缓存数据
     */
    void loadingConfigCache();

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     */
    boolean insertConfig(SysConfig config);

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     */
    boolean updateConfig(SysConfig config);

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    void deleteByIds(List<Long> configIds);

    /**
     * 校验参数键名是否唯一
     *
     * @param configId  参数ID
     * @param configKey 参数key
     * @return 结果
     */
    boolean checkUsedByConfigKey(@Nullable Long configId, @Nonnull String configKey);
}
