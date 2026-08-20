package com.wzkris.usercenter.api.oauth2;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.oauth2.request.ClientSecretUpdateRequest;
import com.wzkris.usercenter.api.oauth2.request.OAuth2ClientMngPageRequest;
import com.wzkris.usercenter.api.oauth2.request.OAuth2ClientMngSaveRequest;
import com.wzkris.usercenter.api.oauth2.request.OAuth2ClientMngUpdateRequest;
import com.wzkris.usercenter.api.oauth2.response.OAuth2ClientMngQueryResponse;
import com.wzkris.usercenter.api.oauth2.response.OAuth2ClientMngPageResponse;

public interface OAuth2ClientMngApi {

    Result<Page<OAuth2ClientMngPageResponse>> queryPage(OAuth2ClientMngPageRequest request);

    Result<OAuth2ClientMngQueryResponse> queryById(IdRequest request);

    Result<Void> update(OAuth2ClientMngUpdateRequest request);

    Result<Void> updateSecret(ClientSecretUpdateRequest request);

    Result<String> save(OAuth2ClientMngSaveRequest request);

    Result<Void> remove(IdRequest request);

}
