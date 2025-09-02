package com.wzkris.system.service;

import com.wzkris.system.domain.SysDict;

public interface SysDictService {

    /**
     * 加载字典缓存数据
     */
    void loadingDictCache();

    SysDict.DictData[] getValueByKey(String dictKey);

    boolean insertDict(SysDict dict);

    boolean updateDict(SysDict dict);

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
