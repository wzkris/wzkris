package com.wzkris.system.api.config;

import com.wzkris.common.core.model.Result;
import com.wzkris.system.request.config.ConfigInfoQueryRequest;

public interface ConfigInfoApi {

    Result<String> queryValue(ConfigInfoQueryRequest request);

}
