package com.wzkris.system.service;

import com.wzkris.system.domain.SysDictData;
import com.wzkris.system.domain.SysDictType;

import java.util.List;

/**
 * 字典 业务层
 *
 * @author wzkris
 */
public interface SysDictTypeService {

    List<SysDictType> list(SysDictType sysDictType);

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    List<SysDictData> listDictDataByType(String dictType);

    /**
     * 批量删除字典信息
     *
     * @param dictIds 需要删除的字典ID
     */
    void deleteDictTypeByIds(Long[] dictIds);

    /**
     * 加载字典缓存数据
     */
    void loadingDictCache();

    /**
     * 清空字典缓存数据
     */
    void clearDictCache();

    /**
     * 重置字典缓存数据
     */
    void resetDictCache();

    /**
     * 新增保存字典类型信息
     *
     * @param sysDictType 字典类型信息
     * @return 结果
     */
    int insertDictType(SysDictType sysDictType);

    /**
     * 修改保存字典类型信息
     *
     * @param sysDictType 字典类型信息
     * @return 结果
     */
    int updateDictType(SysDictType sysDictType);

    /**
     * 校验字典类型称是否唯一
     *
     * @param sysDictType 字典类型
     * @return 结果
     */
    boolean checkDictTypeUnique(SysDictType sysDictType);

}
