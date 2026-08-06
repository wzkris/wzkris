package com.wzkris.usercenter.api.config;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.config.request.ConfigMngPageRequest;
import com.wzkris.usercenter.api.config.request.ConfigMngSaveRequest;
import com.wzkris.usercenter.api.config.request.ConfigMngUpdateRequest;
import com.wzkris.usercenter.api.config.response.ConfigMngQueryResponse;
import com.wzkris.usercenter.api.config.response.ConfigMngPageResponse;

public interface ConfigMngApi {

    Result<Page<ConfigMngPageResponse>> queryPage(ConfigMngPageRequest request);

    Result<ConfigMngQueryResponse> queryInfo(IdRequest request);

    Result<Void> save(ConfigMngSaveRequest request);

    Result<Void> update(ConfigMngUpdateRequest request);

    Result<Void> remove(IdRequest request);

    Result<Void> refreshCache();

}
