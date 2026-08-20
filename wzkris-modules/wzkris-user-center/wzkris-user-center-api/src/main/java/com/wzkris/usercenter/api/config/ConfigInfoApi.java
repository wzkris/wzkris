package com.wzkris.usercenter.api.config;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.api.config.request.ConfigInfoQueryRequest;

public interface ConfigInfoApi {

    Result<String> queryValue(ConfigInfoQueryRequest request);

}
