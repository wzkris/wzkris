package com.thingslink.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.common.core.exception.BusinessExceptionI18n;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.system.domain.DictData;
import com.thingslink.system.domain.DictType;
import com.thingslink.system.mapper.DictDataMapper;
import com.thingslink.system.mapper.DictTypeMapper;
import com.thingslink.system.service.DictTypeService;
import com.thingslink.system.utils.DictUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class DictTypeServiceImpl implements DictTypeService {
    private final DictTypeMapper dictTypeMapper;
    private final DictDataMapper dictDataMapper;

    @Override
    public List<DictType> list(DictType dictType) {
        LambdaQueryWrapper<DictType> lqw = new LambdaQueryWrapper<DictType>()
                .like(StringUtil.isNotBlank(dictType.getDictName()), DictType::getDictName, dictType.getDictName())
                .eq(StringUtil.isNotBlank(dictType.getStatus()), DictType::getStatus, dictType.getStatus())
                .like(StringUtil.isNotBlank(dictType.getDictType()), DictType::getDictType, dictType.getDictType());
        return dictTypeMapper.selectList(lqw);
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<DictData> listDictDataByType(String dictType) {
        List<DictData> dictDatas = DictUtil.getDictCache(dictType);
        if (StringUtil.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        dictDatas = dictDataMapper.listByType(dictType);
        if (StringUtil.isNotEmpty(dictDatas)) {
            DictUtil.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     */
    @Override
    public void deleteDictTypeByIds(Long[] dictIds) {
        for (Long dictId : dictIds) {
            DictType dictType = dictTypeMapper.selectById(dictId);
            if (dictDataMapper.countByType(dictType.getDictType()) > 0) {
                throw new BusinessExceptionI18n("business.allocated", dictType.getDictName());
            }
            dictTypeMapper.deleteById(dictId);
            DictUtil.removeDictCache(dictType.getDictType());
        }
    }

    /**
     * 加载字典缓存数据
     */
    @PostConstruct
    @Override
    public void loadingDictCache() {
        LambdaQueryWrapper<DictData> lqw = new LambdaQueryWrapper<DictData>().eq(DictData::getStatus, "0");
        Map<String, List<DictData>> dictDataMap = dictDataMapper.selectList(lqw).stream().collect(Collectors.groupingBy(DictData::getDictType));
        for (Map.Entry<String, List<DictData>> entry : dictDataMap.entrySet()) {
            DictUtil.setDictCache(entry.getKey(), entry.getValue().stream().sorted(Comparator.comparing(DictData::getDictSort)).collect(Collectors.toList()));
        }
    }

    /**
     * 清空字典缓存数据
     */
    @Override
    public void clearDictCache() {
        DictUtil.clearDictCache();
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @Override
    public int insertDictType(DictType dict) {
        int row = dictTypeMapper.insert(dict);
        if (row > 0) {
            DictUtil.setDictCache(dict.getDictType(), null);
        }
        return row;
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDictType(DictType dict) {
        DictType oldDict = dictTypeMapper.selectById(dict.getDictId());
        dictDataMapper.updateDictDataType(oldDict.getDictType(), dict.getDictType());
        int row = dictTypeMapper.updateById(dict);
        if (row > 0) {
            List<DictData> dictDatas = dictDataMapper.listByType(dict.getDictType());
            DictUtil.setDictCache(dict.getDictType(), dictDatas);
        }
        return row;
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    @Override
    public boolean checkDictTypeUnique(DictType dictType) {
        LambdaQueryWrapper<DictType> lqw = new LambdaQueryWrapper<DictType>()
                .eq(DictType::getDictType, dictType.getDictType())
                .ne(DictType::getDictId, dictType.getDictId());
        return dictTypeMapper.exists(lqw);
    }
}
