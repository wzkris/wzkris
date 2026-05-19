package com.wzkris.auth.api.online;

import com.wzkris.auth.api.online.request.SidRequest;
import com.wzkris.auth.api.online.response.OnlineSessionResponse;
import com.wzkris.common.core.model.Result;

import java.util.Collection;

public interface OnlineSessionApi {

    Result<Collection<OnlineSessionResponse>> queryList();

    Result<Void> kickout(SidRequest request);

}
