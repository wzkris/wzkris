package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.system.domain.DictionaryInfoDO;
import com.wzkris.system.mapper.DictionaryInfoMapper;
import com.wzkris.system.service.DictionaryInfoService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DictionaryInfoServiceImpl implements DictionaryInfoService {

    private static final String DICT_KEY = "system_dictionary";

    private final DictionaryInfoMapper dictionaryInfoMapper;

    private RMap<String, DictionaryInfoDO.DictData[]> cache() {
        return RedisUtil.getRMap(DICT_KEY);
    }

    @PostConstruct
    @Override
    public void loadingDictCache() {
        RMap<String, DictionaryInfoDO.DictData[]> rMap = cache();
        Map<String, DictionaryInfoDO.DictData[]> map = dictionaryInfoMapper.selectList(null).stream()
                .collect(Collectors.toMap(DictionaryInfoDO::getDictKey, DictionaryInfoDO::getDictValue));
        rMap.clear();
        rMap.putAll(map);
    }

    @Override
    public DictionaryInfoDO.DictData[] getValueByKey(String dictKey) {
        DictionaryInfoDO.DictData[] dictData = cache().get(dictKey);
        if (dictData != null) {
            return dictData;
        }
        DictionaryInfoDO dict = dictionaryInfoMapper.selectByDictKey(dictKey);
        if (dict == null) {
            return new DictionaryInfoDO.DictData[]{};
        }
        cache().put(dictKey, dict.getDictValue());
        return dict.getDictValue();
    }

    @Override
    public boolean insertDict(DictionaryInfoDO dict) {
        boolean success = dictionaryInfoMapper.insert(dict) > 0;
        if (success && dict.getDictValue() != null) {
            cache().put(dict.getDictKey(), dict.getDictValue());
        }
        return success;
    }

    @Override
    public boolean updateDict(DictionaryInfoDO dict) {
        boolean success = dictionaryInfoMapper.updateById(dict) > 0;
        if (success && dict.getDictValue() != null) {
            cache().put(dict.getDictKey(), dict.getDictValue());
        }
        return success;
    }

    @Override
    public boolean deleteById(Long dictId) {
        DictionaryInfoDO dictionaryInfoDO = dictionaryInfoMapper.selectById(dictId);
        boolean success = dictionaryInfoMapper.deleteById(dictId) > 0;
        if (success) {
            cache().remove(dictionaryInfoDO.getDictKey());
        }
        return success;
    }

    @Override
    public boolean checkUsedByDictKey(Long dictId, String dictKey) {
        LambdaQueryWrapper<DictionaryInfoDO> lqw = new LambdaQueryWrapper<DictionaryInfoDO>()
                .eq(DictionaryInfoDO::getDictId, dictId)
                .ne(Objects.nonNull(dictId), DictionaryInfoDO::getDictKey, dictKey);
        return dictionaryInfoMapper.exists(lqw);
    }

}
