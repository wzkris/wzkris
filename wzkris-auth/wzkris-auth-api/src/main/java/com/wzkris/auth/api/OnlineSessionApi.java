package com.wzkris.auth.api;

import com.wzkris.auth.response.OnlineSessionResponse;
import com.wzkris.common.core.model.Result;

import java.util.Collection;

public interface OnlineSessionApi {

    Result<Collection<OnlineSessionResponse>> queryList();

    Result<Void> kickout(String sid);

}
