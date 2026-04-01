package com.wzkris.system.api.dictionary;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.request.dictionary.DictionaryMngQueryRequest;
import com.wzkris.system.request.dictionary.DictionaryMngSaveRequest;
import com.wzkris.system.request.dictionary.DictionaryMngUpdateRequest;
import com.wzkris.system.response.dictionary.DictionaryInfoResponse;

public interface DictionaryMngApi {

    Result<Page<DictionaryInfoResponse>> queryPage(DictionaryMngQueryRequest request);

    Result<DictionaryInfoResponse> queryInfo(Long dictId);

    Result<Void> save(DictionaryMngSaveRequest addReq);

    Result<Void> update(DictionaryMngUpdateRequest request);

    Result<Void> remove(Long dictId);

    Result<?> refreshCache();

}
