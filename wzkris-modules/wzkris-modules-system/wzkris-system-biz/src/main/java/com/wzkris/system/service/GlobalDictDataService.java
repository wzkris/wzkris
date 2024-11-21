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
    void deleteDictData(List<Long> dataIds);

    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     */
    void insertDictData(GlobalDictData dictData);

    /**
     * 修改保存字典数据信息
     *
     * @param globalDictData 字典数据信息
     */
    void updateDictData(GlobalDictData globalDictData);

}
