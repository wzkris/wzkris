package com.wzkris.auth.api;

import com.wzkris.auth.request.WexcxSwitchRequest;
import com.wzkris.common.core.model.Result;

public interface SwitchTokenApi {

    Result<?> switchTenantToken(WexcxSwitchRequest request);

}