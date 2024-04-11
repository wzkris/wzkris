package com.thingslink.system.service;

import com.thingslink.system.domain.DictData;
import com.thingslink.system.domain.DictType;

import java.util.List;

/**
 * 字典 业务层
 *
 * @author wzkris
 */
public interface DictTypeService {

    List<DictType> list(DictType dictType);

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    List<DictData> listDictDataByType(String dictType);

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
     * @param dictType 字典类型信息
     * @return 结果
     */
    int insertDictType(DictType dictType);

    /**
     * 修改保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    int updateDictType(DictType dictType);

    /**
     * 校验字典类型称是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    boolean checkDictTypeUnique(DictType dictType);

}
