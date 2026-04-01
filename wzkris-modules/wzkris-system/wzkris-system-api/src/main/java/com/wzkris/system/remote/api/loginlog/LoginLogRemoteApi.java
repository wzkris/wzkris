package com.wzkris.system.remote.api.loginlog;

import com.wzkris.common.core.model.Result;
import com.wzkris.system.remote.api.loginlog.request.LoginLogEventRequest;

import java.util.List;

public interface LoginLogRemoteApi {

    Result<Void> save(List<LoginLogEventRequest> loginLogEventRequests);

}

