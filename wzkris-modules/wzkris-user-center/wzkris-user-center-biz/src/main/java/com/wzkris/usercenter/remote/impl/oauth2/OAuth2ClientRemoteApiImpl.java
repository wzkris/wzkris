package com.wzkris.usercenter.remote.impl.oauth2;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.domain.OAuth2ClientDO;
import com.wzkris.usercenter.mapper.OAuth2ClientMapper;
import com.wzkris.usercenter.remote.api.oauth2.OAuth2ClientRemoteApi;
import com.wzkris.usercenter.remote.api.oauth2.response.OAuth2ClientResponse;
import com.wzkris.usercenter.request.common.StringValueRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2ClientRemoteApiImpl implements OAuth2ClientRemoteApi {

    private final OAuth2ClientMapper oAuth2ClientMapper;

    @Override
    public Result<OAuth2ClientResponse> queryById(StringValueRequest request) {
        OAuth2ClientDO oauth2ClientDO = oAuth2ClientMapper.selectById(request.getValue());
        return Result.ok(this.toOAuth2ClientResponse(oauth2ClientDO));
    }

    @Override
    public Result<OAuth2ClientResponse> queryByClientId(StringValueRequest request) {
        OAuth2ClientDO oauth2ClientDO = oAuth2ClientMapper.selectByClientId(request.getValue());
        return Result.ok(this.toOAuth2ClientResponse(oauth2ClientDO));
    }

    private OAuth2ClientResponse toOAuth2ClientResponse(OAuth2ClientDO oauth2ClientDO) {
        if (oauth2ClientDO == null) {
            return null;
        }
        OAuth2ClientResponse response = new OAuth2ClientResponse();
        response.setId(oauth2ClientDO.getId());
        response.setClientId(oauth2ClientDO.getClientId());
        response.setClientSecret(oauth2ClientDO.getClientSecret());
        response.setScopes(oauth2ClientDO.getScopes());
        response.setAuthorizationGrantTypes(oauth2ClientDO.getAuthorizationGrantTypes());
        response.setRedirectUris(oauth2ClientDO.getRedirectUris());
        response.setStatus(oauth2ClientDO.getStatus());
        response.setAutoApprove(oauth2ClientDO.getAutoApprove());
        return response;
    }

}

