package com.wzkris.usercenter.remote.impl.oauth2;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.usercenter.domain.OAuth2ClientDO;
import com.wzkris.usercenter.mapper.OAuth2ClientMapper;
import com.wzkris.usercenter.remote.api.oauth2.OAuth2ClientRemoteApi;
import com.wzkris.usercenter.remote.api.oauth2.request.OAuth2ClientQueryRequest;
import com.wzkris.usercenter.remote.api.oauth2.response.OAuth2ClientListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OAuth2ClientRemoteApiImpl implements OAuth2ClientRemoteApi {

    private final OAuth2ClientMapper oAuth2ClientMapper;

    @Override
    public Result<List<OAuth2ClientListResponse>> queryList(OAuth2ClientQueryRequest request) {
        LambdaQueryWrapper<OAuth2ClientDO> eq = Wrappers.lambdaQuery(OAuth2ClientDO.class)
                .eq(StringUtil.isNotBlank(request.getId()), OAuth2ClientDO::getId, request.getId())
                .eq(StringUtil.isNotBlank(request.getClientId()), OAuth2ClientDO::getClientId, request.getClientId());
        List<OAuth2ClientDO> list = oAuth2ClientMapper.selectList(eq);
        List<OAuth2ClientListResponse> responseList = new ArrayList<>();
        for (OAuth2ClientDO oauth2ClientDO : list) {
            responseList.add(this.toOAuth2ClientListResponse(oauth2ClientDO));
        }
        return Result.ok(responseList);
    }

    private OAuth2ClientListResponse toOAuth2ClientListResponse(OAuth2ClientDO oauth2ClientDO) {
        if (oauth2ClientDO == null) {
            return null;
        }
        OAuth2ClientListResponse response = new OAuth2ClientListResponse();
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

