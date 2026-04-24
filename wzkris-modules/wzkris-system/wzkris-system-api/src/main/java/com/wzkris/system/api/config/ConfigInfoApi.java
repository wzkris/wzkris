package com.wzkris.system.api.config;

import com.wzkris.common.core.model.Result;
import com.wzkris.system.request.config.ConfigMngQueryRequest;

public interface ConfigInfoApi {

    Result<String> queryValue(ConfigMngQueryRequest request);

}
