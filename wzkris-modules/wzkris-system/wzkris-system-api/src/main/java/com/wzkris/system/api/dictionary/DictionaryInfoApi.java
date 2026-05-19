package com.wzkris.system.api.dictionary;

import com.wzkris.common.core.model.Result;
import com.wzkris.system.api.dictionary.request.DictionaryInfoListRequest;
import com.wzkris.system.api.dictionary.response.DictionaryDataResponse;

import java.util.List;

public interface DictionaryInfoApi {

    Result<List<DictionaryDataResponse>> queryValue(DictionaryInfoListRequest request);

}
