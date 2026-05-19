package com.wzkris.system.api.dictionary;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.system.api.dictionary.request.DictionaryMngPageRequest;
import com.wzkris.system.api.dictionary.request.DictionaryMngSaveRequest;
import com.wzkris.system.api.dictionary.request.DictionaryMngUpdateRequest;
import com.wzkris.system.api.dictionary.response.DictionaryMngResponse;

public interface DictionaryMngApi {

    Result<Page<DictionaryMngResponse>> queryPage(DictionaryMngPageRequest request);

    Result<DictionaryMngResponse> queryInfo(IdRequest request);

    Result<Void> save(DictionaryMngSaveRequest request);

    Result<Void> update(DictionaryMngUpdateRequest request);

    Result<Void> remove(IdRequest request);

    Result<?> refreshCache();

}
