package com.wzkris.system.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.system.domain.GlobalDictData;
import com.wzkris.system.domain.GlobalDictType;
import com.wzkris.system.listener.event.RefreshDictEvent;
import com.wzkris.system.mapper.GlobalDictDataMapper;
import com.wzkris.system.mapper.GlobalDictTypeMapper;
import com.wzkris.system.service.GlobalDictTypeService;
import com.wzkris.system.utils.DictCacheUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典 业务层处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class GlobalDictTypeServiceImpl implements GlobalDictTypeService {
    private final GlobalDictTypeMapper dictTypeMapper;
    private final GlobalDictDataMapper dictDataMapper;

    @Override
    public void loadingDictCache() {
        Map<String, List<GlobalDictData>> dictDataMap = dictDataMapper.selectList(null)
                .stream()
                .collect(Collectors.groupingBy(GlobalDictData::getDictType, Collectors.collectingAndThen(Collectors.toList(), list -> {
                    // 对每个List进行排序
                    list.sort(Comparator.comparing(GlobalDictData::getDictSort));
                    return list;
                })));
        for (Map.Entry<String, List<GlobalDictData>> entry : dictDataMap.entrySet()) {
            DictCacheUtil.setDictCache(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean insertDictType(GlobalDictType dictType) {
        boolean success = dictTypeMapper.insert(dictType) > 0;
        if (success) {
            SpringUtil.getContext().publishEvent(new RefreshDictEvent(Collections.singletonList(dictType.getDictType())));
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDictType(GlobalDictType dictType) {
        GlobalDictType oldDict = dictTypeMapper.selectByIdForUpdate(dictType.getTypeId());
        boolean success = dictTypeMapper.updateById(dictType) > 0;
        if (success) {
            if (!StringUtil.equals(oldDict.getDictType(), dictType.getDictType())) {
                dictDataMapper.updateDictByType(oldDict.getDictType(), dictType.getDictType());
            }
            SpringUtil.getContext().publishEvent(new RefreshDictEvent(Collections.singletonList(dictType.getDictType())));
        }
        return success;
    }

    @Override
    public boolean checkDictTypeUnique(@Nullable Long typeId, @Nonnull String dictType) {
        LambdaQueryWrapper<GlobalDictType> lqw = new LambdaQueryWrapper<GlobalDictType>()
                .eq(GlobalDictType::getDictType, dictType)
                .ne(ObjUtil.isNotNull(typeId), GlobalDictType::getTypeId, dictType);
        return dictTypeMapper.exists(lqw);
    }

    @Override
    public boolean checkDictTypeUsed(List<Long> typeIds) {
        List<String> dictTypes = dictTypeMapper.selectByIds(typeIds).stream().map(GlobalDictType::getDictType).toList();
        LambdaQueryWrapper<GlobalDictData> lqw = Wrappers.lambdaQuery(GlobalDictData.class)
                .in(GlobalDictData::getDictType, dictTypes);
        return dictDataMapper.exists(lqw);
    }

    @Override
    public void deleteByIds(List<Long> typeIds) {
        if (dictTypeMapper.deleteByIds(typeIds) > 0) {
            DictCacheUtil.clearAll();
            loadingDictCache();
        }
    }
}
