package com.wzkris.system.impl.dictionary;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.api.dictionary.DictionaryInfoApi;
import com.wzkris.system.domain.DictionaryInfoDO;
import com.wzkris.system.response.dictionary.DictionaryDataResponse;
import com.wzkris.system.service.DictionaryInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DictionaryInfoApiImpl extends BaseController implements DictionaryInfoApi {

    private final DictionaryInfoService dictService;

    @Override
    public Result<List<DictionaryDataResponse>> queryValue(String dictKey) {
        DictionaryInfoDO.DictData[] source = dictService.getValueByKey(dictKey);
        if (source == null) {
            return ok(null);
        }
        List<DictionaryDataResponse> result = new ArrayList<>();
        for (DictionaryInfoDO.DictData dictData : source) {
            result.add(BeanUtil.convert(dictData, DictionaryDataResponse.class));
        }
        return ok(result);
    }

}
