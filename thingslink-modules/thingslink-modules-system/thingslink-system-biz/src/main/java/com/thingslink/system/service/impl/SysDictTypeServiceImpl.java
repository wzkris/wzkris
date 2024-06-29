package com.thingslink.system.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.common.core.exception.BusinessExceptionI18n;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.system.domain.SysDictData;
import com.thingslink.system.domain.SysDictType;
import com.thingslink.system.mapper.SysDictDataMapper;
import com.thingslink.system.mapper.SysDictTypeMapper;
import com.thingslink.system.service.SysDictTypeService;
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
public class SysDictTypeServiceImpl implements SysDictTypeService {
    private final SysDictTypeMapper sysDictTypeMapper;
    private final SysDictDataMapper sysDictDataMapper;

    @Override
    public List<SysDictType> list(SysDictType sysDictType) {
        LambdaQueryWrapper<SysDictType> lqw = new LambdaQueryWrapper<SysDictType>()
                .like(StringUtil.isNotBlank(sysDictType.getDictName()), SysDictType::getDictName, sysDictType.getDictName())
                .like(StringUtil.isNotBlank(sysDictType.getDictType()), SysDictType::getDictType, sysDictType.getDictType());
        return sysDictTypeMapper.selectList(lqw);
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> listDictDataByType(String dictType) {
        List<SysDictData> sysDictData = DictUtil.getDictCache(dictType);
        if (StringUtil.isNotEmpty(sysDictData)) {
            return sysDictData;
        }
        sysDictData = sysDictDataMapper.listByType(dictType);
        if (StringUtil.isNotEmpty(sysDictData)) {
            DictUtil.setDictCache(dictType, sysDictData);
            return sysDictData;
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
            SysDictType sysDictType = sysDictTypeMapper.selectById(dictId);
            if (sysDictDataMapper.countByType(sysDictType.getDictType()) > 0) {
                throw new BusinessExceptionI18n("business.allocated", sysDictType.getDictName());
            }
            sysDictTypeMapper.deleteById(dictId);
            DictUtil.removeDictCache(sysDictType.getDictType());
        }
    }

    /**
     * 加载字典缓存数据
     */
    @PostConstruct
    @Override
    public void loadingDictCache() {
        Map<String, List<SysDictData>> dictDataMap = sysDictDataMapper.selectList(null).stream().collect(Collectors.groupingBy(SysDictData::getDictType));
        for (Map.Entry<String, List<SysDictData>> entry : dictDataMap.entrySet()) {
            DictUtil.setDictCache(entry.getKey(), entry.getValue().stream().sorted(Comparator.comparing(SysDictData::getDictSort)).collect(Collectors.toList()));
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
    public int insertDictType(SysDictType dict) {
        int row = sysDictTypeMapper.insert(dict);
        if (row > 0) {
            List<SysDictData> sysDictData = sysDictDataMapper.listByType(dict.getDictType());
            DictUtil.setDictCache(dict.getDictType(), sysDictData);
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
    public int updateDictType(SysDictType dict) {
        SysDictType oldDict = sysDictTypeMapper.selectById(dict.getTypeId());
        sysDictDataMapper.updateDictDataType(oldDict.getDictType(), dict.getDictType());
        int row = sysDictTypeMapper.updateById(dict);
        if (row > 0) {
            List<SysDictData> sysDictData = sysDictDataMapper.listByType(dict.getDictType());
            DictUtil.setDictCache(dict.getDictType(), sysDictData);
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
    public boolean checkDictTypeUnique(SysDictType dictType) {
        LambdaQueryWrapper<SysDictType> lqw = new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getDictType, dictType.getDictType())
                .ne(ObjUtil.isNotNull(dictType.getTypeId()), SysDictType::getTypeId, dictType.getTypeId());
        return sysDictTypeMapper.exists(lqw);
    }
}
