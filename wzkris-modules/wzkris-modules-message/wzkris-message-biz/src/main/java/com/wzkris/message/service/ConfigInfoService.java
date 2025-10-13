package com.wzkris.message.service;

import com.wzkris.message.domain.ConfigInfoDO;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * 参数配置 服务层
 *
 * @author wzkris
 */
public interface ConfigInfoService {

    /**
     * 加载参数缓存数据
     */
    void loadingConfigCache();

    /**
     * 先查缓存在查db
     */
    String getValueByKey(String configkey);

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     */
    boolean insertConfig(ConfigInfoDO config);

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     */
    boolean updateConfig(ConfigInfoDO config);

    /**
     * 批量删除参数信息
     *
     * @param configId 参数ID
     */
    boolean deleteById(Long configId);

    /**
     * 校验参数键名是否唯一
     *
     * @param configId  参数ID
     * @param configKey 参数key
     * @return 结果
     */
    boolean checkUsedByConfigKey(@Nullable Long configId, @Nonnull String configKey);

}
