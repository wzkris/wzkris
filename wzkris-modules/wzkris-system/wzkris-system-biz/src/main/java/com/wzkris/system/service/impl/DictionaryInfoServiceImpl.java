package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.system.domain.DictionaryInfoDO;
import com.wzkris.system.mapper.DictionaryInfoMapper;
import com.wzkris.system.service.DictionaryInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DictionaryInfoServiceImpl implements DictionaryInfoService, SmartInitializingSingleton {

    private static final String DICT_KEY = "system-dictionary";

    private final DictionaryInfoMapper dictionaryInfoMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void afterSingletonsInstantiated() {
        loadingDictCache();
    }

    @Override
    public void loadingDictCache() {
        Map<String, DictionaryInfoDO.DictData[]> map = dictionaryInfoMapper.selectList(null).stream()
                .collect(Collectors.toMap(DictionaryInfoDO::getDictKey, DictionaryInfoDO::getDictValue));
        redisTemplate.delete(DICT_KEY);
        if (!map.isEmpty()) {
            redisTemplate.opsForHash().putAll(DICT_KEY, (Map) map);
        }
    }

    @Override
    public DictionaryInfoDO.DictData[] getValueByKey(String dictKey) {
        Object value = redisTemplate.opsForHash().get(DICT_KEY, dictKey);
        if (value instanceof DictionaryInfoDO.DictData[]) {
            return (DictionaryInfoDO.DictData[]) value;
        }
        DictionaryInfoDO dict = dictionaryInfoMapper.selectByDictKey(dictKey);
        if (dict == null) {
            return new DictionaryInfoDO.DictData[0];
        }
        redisTemplate.opsForHash().put(DICT_KEY, dictKey, dict.getDictValue());
        return dict.getDictValue();
    }

    @Override
    public boolean insertDict(DictionaryInfoDO dict) {
        boolean success = dictionaryInfoMapper.insert(dict) > 0;
        if (success && dict.getDictValue() != null) {
            redisTemplate.opsForHash().put(DICT_KEY, dict.getDictKey(), dict.getDictValue());
        }
        return success;
    }

    @Override
    public boolean updateDict(DictionaryInfoDO dict) {
        boolean success = dictionaryInfoMapper.updateById(dict) > 0;
        if (success && dict.getDictValue() != null) {
            redisTemplate.opsForHash().put(DICT_KEY, dict.getDictKey(), dict.getDictValue());
        }
        return success;
    }

    @Override
    public boolean deleteById(Long dictId) {
        DictionaryInfoDO dictionaryInfoDO = dictionaryInfoMapper.selectById(dictId);
        boolean success = dictionaryInfoMapper.deleteById(dictId) > 0;
        if (success) {
            redisTemplate.opsForHash().delete(DICT_KEY, dictionaryInfoDO.getDictKey());
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
