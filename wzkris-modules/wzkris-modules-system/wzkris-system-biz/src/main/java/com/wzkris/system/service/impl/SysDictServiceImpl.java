package com.wzkris.system.service.impl;

import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.system.domain.SysDict;
import com.wzkris.system.mapper.SysDictMapper;
import com.wzkris.system.service.SysDictService;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysDictServiceImpl implements SysDictService {

    private static final String DICT_KEY = "sys_dict";

    private final SysDictMapper dictMapper;

    private RMap<String, SysDict.DictData[]> cache() {
        return RedisUtil.getRMap(DICT_KEY);
    }

    @PostConstruct
    @Override
    public void loadingDictCache() {
        RMap<String, SysDict.DictData[]> rMap = cache();
        Map<String, SysDict.DictData[]> map = StreamUtil.of(dictMapper.selectList(null))
                .collect(Collectors.toMap(SysDict::getDictKey, SysDict::getDictValue));
        rMap.clear();
        rMap.putAll(map);
    }

    @Override
    public SysDict.DictData[] getValueByDictKey(String dictKey) {
        SysDict.DictData[] dictData = cache().get(dictKey);
        if (dictData != null) {
            return dictData;
        }
        SysDict dict = dictMapper.selectOne(Wrappers.lambdaQuery(SysDict.class).eq(SysDict::getDictKey, dictKey));
        if (dict == null) {
            return new SysDict.DictData[] {};
        }
        cache().put(dictKey, dict.getDictValue());
        return dict.getDictValue();
    }

    @Override
    public boolean insertDict(SysDict dict) {
        boolean success = dictMapper.insert(dict) > 0;
        if (success && dict.getDictValue() != null) {
            cache().put(dict.getDictKey(), dict.getDictValue());
        }
        return success;
    }

    @Override
    public boolean updateDict(SysDict dict) {
        boolean success = dictMapper.updateById(dict) > 0;
        if (success && dict.getDictValue() != null) {
            cache().put(dict.getDictKey(), dict.getDictValue());
        }
        return success;
    }

    @Override
    public boolean deleteById(Long dictId) {
        SysDict sysDict = dictMapper.selectById(dictId);
        boolean success = dictMapper.deleteById(dictId) > 0;
        if (success) {
            cache().remove(sysDict.getDictKey());
        }
        return success;
    }

    @Override
    public boolean checkUsedByDictKey(Long dictId, String dictKey) {
        LambdaQueryWrapper<SysDict> lqw = new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getDictId, dictId)
                .ne(ObjUtil.isNotNull(dictId), SysDict::getDictKey, dictKey);
        return dictMapper.exists(lqw);
    }
}
