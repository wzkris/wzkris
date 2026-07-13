package com.wzkris.usercenter.api.config;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.config.request.ConfigMngPageRequest;
import com.wzkris.usercenter.api.config.request.ConfigMngSaveRequest;
import com.wzkris.usercenter.api.config.request.ConfigMngUpdateRequest;
import com.wzkris.usercenter.api.config.response.ConfigInfoResponse;

public interface ConfigMngApi {

    Result<Page<ConfigInfoResponse>> queryPage(ConfigMngPageRequest request);

    Result<ConfigInfoResponse> queryInfo(IdRequest request);

    Result<Void> save(ConfigMngSaveRequest request);

    Result<Void> update(ConfigMngUpdateRequest request);

    Result<Void> remove(IdRequest request);

    Result<Void> refreshCache();

}
