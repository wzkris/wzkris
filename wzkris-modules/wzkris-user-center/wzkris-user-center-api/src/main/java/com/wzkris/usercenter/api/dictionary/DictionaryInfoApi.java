package com.wzkris.usercenter.api.dictionary;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.api.dictionary.request.DictionaryInfoListRequest;
import com.wzkris.usercenter.api.dictionary.response.DictionaryDataResponse;

import java.util.List;

public interface DictionaryInfoApi {

    Result<List<DictionaryDataResponse>> queryValue(DictionaryInfoListRequest request);

}
