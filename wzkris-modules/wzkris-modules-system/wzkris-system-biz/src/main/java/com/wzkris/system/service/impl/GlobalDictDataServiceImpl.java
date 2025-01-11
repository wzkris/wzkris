package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.system.domain.GlobalDictData;
import com.wzkris.system.listener.event.RefreshDictEvent;
import com.wzkris.system.mapper.GlobalDictDataMapper;
import com.wzkris.system.service.GlobalDictDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 字典 业务层处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class GlobalDictDataServiceImpl implements GlobalDictDataService {

    private final GlobalDictDataMapper dictDataMapper;

    @Override
    public void deleteDictData(List<Long> dataIds) {
        List<String> dictTypes = dictDataMapper.selectList(Wrappers.lambdaQuery(GlobalDictData.class)
                        .select(GlobalDictData::getDictType)
                        .in(GlobalDictData::getDataId, dataIds))
                .stream().map(GlobalDictData::getDictType).toList();
        if (dictDataMapper.deleteByIds(dataIds) > 0) {
            SpringUtil.getContext().publishEvent(new RefreshDictEvent(dictTypes));
        }
    }

    @Override
    public void insertDictData(GlobalDictData dictData) {
        if (dictDataMapper.insert(dictData) > 0) {
            SpringUtil.getContext().publishEvent(new RefreshDictEvent(Collections.singletonList(dictData.getDictType())));
        }
    }

    @Override
    public void updateDictData(GlobalDictData data) {
        if (dictDataMapper.updateById(data) > 0) {
            SpringUtil.getContext().publishEvent(new RefreshDictEvent(Collections.singletonList(data.getDictType())));
        }
    }
}
