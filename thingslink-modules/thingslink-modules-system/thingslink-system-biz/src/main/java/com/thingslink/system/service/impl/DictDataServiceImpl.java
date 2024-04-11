package com.thingslink.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.system.domain.DictData;
import com.thingslink.system.mapper.DictDataMapper;
import com.thingslink.system.service.DictDataService;
import com.thingslink.system.utils.DictUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典 业务层处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class DictDataServiceImpl implements DictDataService {
    private final DictDataMapper dictDataMapper;

    @Override
    public List<DictData> list(DictData dictData) {
        LambdaQueryWrapper<DictData> lqw = new LambdaQueryWrapper<DictData>()
                .eq(StringUtil.isNotBlank(dictData.getDictType()), DictData::getDictType, dictData.getDictType())
                .like(StringUtil.isNotBlank(dictData.getDictLabel()), DictData::getDictLabel, dictData.getDictLabel())
                .eq(StringUtil.isNotBlank(dictData.getStatus()), DictData::getStatus, dictData.getStatus())
                .orderByAsc(DictData::getDictSort);
        return dictDataMapper.selectList(lqw);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    @Override
    public void deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            DictData data = dictDataMapper.selectById(dictCode);
            dictDataMapper.deleteById(dictCode);
            List<DictData> dictDatas = dictDataMapper.listByType(data.getDictType());
            DictUtil.setDictCache(data.getDictType(), dictDatas);
        }
    }

    /**
     * 新增保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(DictData data) {
        int row = dictDataMapper.insert(data);
        if (row > 0) {
            List<DictData> dictDatas = dictDataMapper.listByType(data.getDictType());
            DictUtil.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(DictData data) {
        int row = dictDataMapper.updateById(data);
        if (row > 0) {
            List<DictData> dictDatas = dictDataMapper.listByType(data.getDictType());
            DictUtil.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }
}
