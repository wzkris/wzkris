package com.wzkris.system.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.system.domain.DictionaryInfoDO;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryInfoMapper extends BaseMapperPlus<DictionaryInfoDO> {

    default DictionaryInfoDO selectByDictKey(String dictKey) {
        return selectOne(Wrappers.lambdaQuery(DictionaryInfoDO.class)
                .eq(DictionaryInfoDO::getDictKey, dictKey));
    }

}
