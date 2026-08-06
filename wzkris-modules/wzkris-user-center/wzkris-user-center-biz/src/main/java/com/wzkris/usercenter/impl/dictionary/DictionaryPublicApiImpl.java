package com.wzkris.usercenter.impl.dictionary;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.dictionary.DictionaryPublicApi;
import com.wzkris.usercenter.api.dictionary.request.DictionaryPublicListRequest;
import com.wzkris.usercenter.api.dictionary.response.DictionaryDataResponse;
import com.wzkris.usercenter.domain.DictionaryInfoDO;
import com.wzkris.usercenter.service.DictionaryInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DictionaryPublicApiImpl extends AbstractApi implements DictionaryPublicApi {

    private final DictionaryInfoService dictService;

    @Override
    public Result<List<DictionaryDataResponse>> queryValue(DictionaryPublicListRequest request) {
        String dictKey = request.getDictKey();
        DictionaryInfoDO.DictData[] source = dictService.getValueByKey(dictKey);
        if (source == null) {
            return ok(null);
        }
        List<DictionaryDataResponse> result = new ArrayList<>();
        for (DictionaryInfoDO.DictData dictData : source) {
            result.add(BeanCopierUtil.copy(dictData, DictionaryDataResponse.class));
        }
        return ok(result);
    }

}
