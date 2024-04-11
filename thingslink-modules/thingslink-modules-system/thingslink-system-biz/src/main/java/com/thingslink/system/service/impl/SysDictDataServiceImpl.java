package com.thingslink.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.system.domain.SysDictData;
import com.thingslink.system.mapper.SysDictDataMapper;
import com.thingslink.system.service.SysDictDataService;
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
public class SysDictDataServiceImpl implements SysDictDataService {
    private final SysDictDataMapper sysDictDataMapper;

    @Override
    public List<SysDictData> list(SysDictData sysDictData) {
        LambdaQueryWrapper<SysDictData> lqw = new LambdaQueryWrapper<SysDictData>()
                .eq(StringUtil.isNotBlank(sysDictData.getDictType()), SysDictData::getDictType, sysDictData.getDictType())
                .like(StringUtil.isNotBlank(sysDictData.getDictLabel()), SysDictData::getDictLabel, sysDictData.getDictLabel())
                .orderByAsc(SysDictData::getDictSort);
        return sysDictDataMapper.selectList(lqw);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    @Override
    public void deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            SysDictData data = sysDictDataMapper.selectById(dictCode);
            sysDictDataMapper.deleteById(dictCode);
            List<SysDictData> sysDictData = sysDictDataMapper.listByType(data.getDictType());
            DictUtil.setDictCache(data.getDictType(), sysDictData);
        }
    }

    /**
     * 新增保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(SysDictData data) {
        int row = sysDictDataMapper.insert(data);
        if (row > 0) {
            List<SysDictData> sysDictData = sysDictDataMapper.listByType(data.getDictType());
            DictUtil.setDictCache(data.getDictType(), sysDictData);
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
    public int updateDictData(SysDictData data) {
        int row = sysDictDataMapper.updateById(data);
        if (row > 0) {
            List<SysDictData> sysDictData = sysDictDataMapper.listByType(data.getDictType());
            DictUtil.setDictCache(data.getDictType(), sysDictData);
        }
        return row;
    }
}