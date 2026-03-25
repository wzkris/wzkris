package com.wzkris.auth.remote.api.loginuser;

import com.wzkris.auth.remote.api.loginuser.request.LoginUserQueryRequest;
import com.wzkris.auth.remote.api.loginuser.request.OAuth2TokenQueryRequest;
import com.wzkris.auth.remote.api.loginuser.response.LoginUserResponse;
import com.wzkris.common.core.model.Result;

public interface LoginUserRemoteApi {

    Result<LoginUserResponse> queryInfo(LoginUserQueryRequest request);

    Result<LoginUserResponse> queryOAuth2(OAuth2TokenQueryRequest request);

}
