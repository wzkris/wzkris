package com.wzkris.message.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.message.domain.DictionaryInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DictionaryInfoMapper extends BaseMapperPlus<DictionaryInfoDO> {

    default DictionaryInfoDO selectByDictKey(String dictKey) {
        return selectOne(Wrappers.lambdaQuery(DictionaryInfoDO.class)
                .eq(DictionaryInfoDO::getDictKey, dictKey));
    }

}
