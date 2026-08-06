package com.wzkris.usercenter.api.dictionary;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.api.dictionary.request.DictionaryPublicListRequest;
import com.wzkris.usercenter.api.dictionary.response.DictionaryPublicListResponse;

import java.util.List;

public interface DictionaryPublicApi {

    Result<List<DictionaryPublicListResponse>> queryValue(DictionaryPublicListRequest request);

}
