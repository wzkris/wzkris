package com.wzkris.system.service;

import com.wzkris.system.domain.GlobalDictType;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

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
     * 校验字典类型是否唯一
     *
     * @param typeId   字典ID
     * @param dictType 字典类型
     * @return 结果
     */
    boolean checkDictTypeUnique(@Nullable Long typeId, @Nonnull String dictType);

    /**
     * 校验字典类型是否被使用
     *
     * @param typeIds 字典类型ID
     * @return 结果
     */
    boolean checkDictTypeUsed(List<Long> typeIds);

    /**
     * 批量删除字典信息
     *
     * @param typeIds 需要删除的字典ID
     */
    void deleteByIds(List<Long> typeIds);
}
