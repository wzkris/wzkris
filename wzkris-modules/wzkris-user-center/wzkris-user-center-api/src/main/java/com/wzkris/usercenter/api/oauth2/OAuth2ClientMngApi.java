package com.wzkris.usercenter.api.oauth2;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.oauth2.ClientSecretUpdateRequest;
import com.wzkris.usercenter.request.oauth2.OAuth2ClientMngQueryRequest;
import com.wzkris.usercenter.request.oauth2.OAuth2ClientMngSaveRequest;
import com.wzkris.usercenter.request.oauth2.OAuth2ClientMngUpdateRequest;
import com.wzkris.usercenter.response.oauth2.OAuth2ClientMngResponse;

public interface OAuth2ClientMngApi {

    Result<Page<OAuth2ClientMngResponse>> queryPage(OAuth2ClientMngQueryRequest request);

    Result<OAuth2ClientMngResponse> queryInfo(Long id);

    Result<Void> update(OAuth2ClientMngUpdateRequest request);

    Result<Void> updateSecret(ClientSecretUpdateRequest request);

    Result<Void> updateStatus(StatusUpdateRequest request);

    Result<String> save(OAuth2ClientMngSaveRequest request);

    Result<Void> remove(Long id);

}
