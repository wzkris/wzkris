package com.wzkris.auth.api.token;

import com.wzkris.auth.api.token.request.WexcxSwitchRequest;
import com.wzkris.common.core.model.Result;

public interface SwitchTokenApi {

    Result<?> switchTenantToken(WexcxSwitchRequest request);

}