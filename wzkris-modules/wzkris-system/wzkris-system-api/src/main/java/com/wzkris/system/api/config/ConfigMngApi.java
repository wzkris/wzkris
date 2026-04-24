package com.wzkris.system.api.config;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.request.common.IdRequest;
import com.wzkris.system.request.config.ConfigMngPageRequest;
import com.wzkris.system.request.config.ConfigMngSaveRequest;
import com.wzkris.system.request.config.ConfigMngUpdateRequest;
import com.wzkris.system.response.config.ConfigInfoResponse;

public interface ConfigMngApi {

    Result<Page<ConfigInfoResponse>> queryPage(ConfigMngPageRequest request);

    Result<ConfigInfoResponse> queryInfo(IdRequest request);

    Result<Void> save(ConfigMngSaveRequest request);

    Result<Void> update(ConfigMngUpdateRequest request);

    Result<Void> remove(IdRequest request);

    Result<Void> refreshCache();

}
