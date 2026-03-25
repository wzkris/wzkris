package com.wzkris.system.api.config;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.request.config.ConfigMngQueryRequest;
import com.wzkris.system.request.config.ConfigMngSaveRequest;
import com.wzkris.system.request.config.ConfigMngUpdateRequest;
import com.wzkris.system.response.config.ConfigInfoResponse;

public interface ConfigMngApi {

    Result<Page<ConfigInfoResponse>> queryPage(ConfigMngQueryRequest request);

    Result<ConfigInfoResponse> queryInfo(Long configId);

    Result<Void> save(ConfigMngSaveRequest request);

    Result<Void> update(ConfigMngUpdateRequest request);

    Result<Void> remove(Long configId);

    Result<Void> refreshCache();

}
