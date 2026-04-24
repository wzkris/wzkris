package com.wzkris.usercenter.api.oauth2;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.request.common.IdRequest;
import com.wzkris.usercenter.request.oauth2.ClientSecretUpdateRequest;
import com.wzkris.usercenter.request.oauth2.OAuth2ClientMngPageRequest;
import com.wzkris.usercenter.request.oauth2.OAuth2ClientMngSaveRequest;
import com.wzkris.usercenter.request.oauth2.OAuth2ClientMngUpdateRequest;
import com.wzkris.usercenter.response.oauth2.OAuth2ClientMngResponse;

public interface OAuth2ClientMngApi {

    Result<Page<OAuth2ClientMngResponse>> queryPage(OAuth2ClientMngPageRequest request);

    Result<OAuth2ClientMngResponse> queryInfo(IdRequest request);

    Result<Void> update(OAuth2ClientMngUpdateRequest request);

    Result<Void> updateSecret(ClientSecretUpdateRequest request);

    Result<String> save(OAuth2ClientMngSaveRequest request);

    Result<Void> remove(IdRequest request);

}
