package com.wzkris.usercenter.api.dictionary;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.dictionary.request.DictionaryMngPageRequest;
import com.wzkris.usercenter.api.dictionary.request.DictionaryMngSaveRequest;
import com.wzkris.usercenter.api.dictionary.request.DictionaryMngUpdateRequest;
import com.wzkris.usercenter.api.dictionary.response.DictionaryMngQueryResponse;
import com.wzkris.usercenter.api.dictionary.response.DictionaryMngPageResponse;

public interface DictionaryMngApi {

    Result<Page<DictionaryMngPageResponse>> queryPage(DictionaryMngPageRequest request);

    Result<DictionaryMngQueryResponse> queryInfo(IdRequest request);

    Result<Void> save(DictionaryMngSaveRequest request);

    Result<Void> update(DictionaryMngUpdateRequest request);

    Result<Void> remove(IdRequest request);

    Result<?> refreshCache();

}
