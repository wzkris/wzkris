package com.wzkris.system.api.config;

import com.wzkris.common.core.model.Result;

public interface ConfigInfoApi {

    Result<String> queryValue(String configKey);

}
