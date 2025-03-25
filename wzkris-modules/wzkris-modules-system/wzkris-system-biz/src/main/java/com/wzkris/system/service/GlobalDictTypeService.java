package com.wzkris.system.service;

import com.wzkris.system.domain.GlobalDictType;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * 字典 业务层
 *
 * @author wzkris
 */
public interface GlobalDictTypeService {

    /**
     * 加载字典缓存数据
     */
    void loadingDictCache();

    /**
     * 新增保存字典类型信息
     *
     * @param globalDictType 字典类型信息
     */
    boolean insertDictType(GlobalDictType globalDictType);

    /**
     * 修改保存字典类型信息
     *
     * @param globalDictType 字典类型信息
     */
    boolean updateDictType(GlobalDictType globalDictType);

    /**
     * 校验字典类型是否被使用
     *
     * @param typeId   字典ID
     * @param dictType 字典类型
     * @return 结果
     */
    boolean checkUsedByDictType(@Nullable Long typeId, @Nonnull String dictType);

    /**
     * 批量删除字典信息
     *
     * @param typeId 字典ID
     */
    boolean deleteById(Long typeId);
}
