package com.wzkris.system.service;

import com.wzkris.system.domain.GlobalDictType;

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
    void insertDictType(GlobalDictType globalDictType);

    /**
     * 修改保存字典类型信息
     *
     * @param globalDictType 字典类型信息
     */
    void updateDictType(GlobalDictType globalDictType);

    /**
     * 校验字典类型是否唯一
     *
     * @param globalDictType 字典类型
     * @return 结果
     */
    boolean checkDictTypeUnique(GlobalDictType globalDictType);

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
