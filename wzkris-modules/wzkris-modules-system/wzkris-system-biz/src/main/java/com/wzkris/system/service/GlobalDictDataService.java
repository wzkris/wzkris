package com.wzkris.system.service;

import com.wzkris.system.domain.GlobalDictData;

import java.util.List;

/**
 * 字典 业务层
 *
 * @author wzkris
 */
public interface GlobalDictDataService {

    /**
     * 批量删除字典数据信息
     *
     * @param dataIds 数据ID
     */
    boolean deleteDictData(List<Long> dataIds);

    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     */
    boolean insertDictData(GlobalDictData dictData);

    /**
     * 修改保存字典数据信息
     *
     * @param globalDictData 字典数据信息
     */
    boolean updateDictData(GlobalDictData globalDictData);

    /**
     * 校验字典类型是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    boolean checkUsedByDictType(String dictType);

}
