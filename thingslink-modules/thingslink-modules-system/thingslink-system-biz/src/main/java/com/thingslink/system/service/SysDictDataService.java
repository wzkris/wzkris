package com.thingslink.system.service;

import com.thingslink.system.domain.SysDictData;

import java.util.List;

/**
 * 字典 业务层
 *
 * @author wzkris
 */
public interface SysDictDataService {

    List<SysDictData> list(SysDictData sysDictData);

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    void deleteDictDataByIds(Long[] dictCodes);

    /**
     * 新增保存字典数据信息
     *
     * @param sysDictData 字典数据信息
     * @return 结果
     */
    int insertDictData(SysDictData sysDictData);

    /**
     * 修改保存字典数据信息
     *
     * @param sysDictData 字典数据信息
     * @return 结果
     */
    int updateDictData(SysDictData sysDictData);

}
