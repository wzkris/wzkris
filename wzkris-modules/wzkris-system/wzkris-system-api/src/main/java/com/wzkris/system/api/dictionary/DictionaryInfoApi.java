package com.wzkris.system.api.dictionary;

import com.wzkris.common.core.model.Result;
import com.wzkris.system.request.dictionary.DictionaryInfoListRequest;
import com.wzkris.system.response.dictionary.DictionaryDataResponse;

import java.util.List;

public interface DictionaryInfoApi {

    Result<List<DictionaryDataResponse>> queryValue(DictionaryInfoListRequest request);

}
