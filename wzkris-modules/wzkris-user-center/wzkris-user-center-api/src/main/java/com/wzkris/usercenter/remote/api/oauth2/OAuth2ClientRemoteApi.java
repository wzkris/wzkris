package com.wzkris.usercenter.remote.api.oauth2;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.oauth2.request.OAuth2ClientQueryRequest;
import com.wzkris.usercenter.remote.api.oauth2.response.OAuth2ClientResponse;

import java.util.List;

public interface OAuth2ClientRemoteApi {

    Result<List<OAuth2ClientResponse>> queryList(OAuth2ClientQueryRequest request);

}

