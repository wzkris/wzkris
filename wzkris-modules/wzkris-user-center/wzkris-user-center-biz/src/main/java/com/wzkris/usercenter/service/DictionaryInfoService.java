package com.wzkris.usercenter.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.usercenter.domain.DictionaryInfoDO;

public interface DictionaryInfoService extends IServicePlus<DictionaryInfoDO> {

    /**
     * 加载字典缓存数据
     */
    void loadingDictCache();

    DictionaryInfoDO.DictData[] getValueByKey(String dictKey);

    boolean insertDict(DictionaryInfoDO dict);

    boolean updateDict(DictionaryInfoDO dict);

    boolean deleteById(Long dictId);

    /**
     * 校验字典键是否被使用
     *
     * @param dictId  字典ID
     * @param dictKey 字典键
     * @return 结果
     */
    boolean checkUsedByDictKey(Long dictId, String dictKey);

}
