package com.wzkris.usercenter.remote.api.oauth2;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.oauth2.request.OAuth2ClientQueryRequest;
import com.wzkris.usercenter.remote.api.oauth2.response.OAuth2ClientResponse;

public interface OAuth2ClientRemoteApi {

    Result<OAuth2ClientResponse> queryOne(OAuth2ClientQueryRequest request);

}

