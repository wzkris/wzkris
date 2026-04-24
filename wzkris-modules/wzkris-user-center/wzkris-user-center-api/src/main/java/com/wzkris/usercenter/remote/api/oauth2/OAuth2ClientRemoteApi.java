package com.wzkris.usercenter.remote.api.oauth2;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.oauth2.response.OAuth2ClientResponse;
import com.wzkris.usercenter.request.common.StringValueRequest;

public interface OAuth2ClientRemoteApi {

    Result<OAuth2ClientResponse> queryById(StringValueRequest request);

    Result<OAuth2ClientResponse> queryByClientId(StringValueRequest request);

}

