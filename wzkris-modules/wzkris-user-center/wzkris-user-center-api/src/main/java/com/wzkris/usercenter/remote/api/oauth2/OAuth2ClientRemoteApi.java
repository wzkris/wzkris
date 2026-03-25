package com.wzkris.usercenter.remote.api.oauth2;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.oauth2.response.OAuth2ClientResponse;

public interface OAuth2ClientRemoteApi {

    Result<OAuth2ClientResponse> queryById(String id);

    Result<OAuth2ClientResponse> queryByClientId(String clientId);

}

