package com.thingslink.user.api_feign;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.security.annotation.InnerAuth;
import com.thingslink.user.api.RemoteOAuth2ClientApi;
import com.thingslink.user.api.domain.dto.Oauth2ClientDTO;
import com.thingslink.user.domain.Oauth2RegisteredClient;
import com.thingslink.user.mapper.Oauth2RegisteredClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static com.thingslink.common.core.domain.Result.success;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 内部OAuth2客户端接口
 * @date : 2024/4/15 16:20
 */
@InnerAuth
@RestController
@RequiredArgsConstructor
public class RemoteOAuth2ClientApiImpl implements RemoteOAuth2ClientApi {
    private final Oauth2RegisteredClientMapper oauth2RegisteredClientMapper;

    /**
     * 查询OAuth2客户端
     */
    @Override
    public Result<Oauth2ClientDTO> getByClientId(String clientId) {
        Oauth2RegisteredClient registeredClient = oauth2RegisteredClientMapper.selectByClientId(clientId);

        Oauth2ClientDTO oauth2ClientDTO = new Oauth2ClientDTO();
        oauth2ClientDTO.setId(registeredClient.getId());
        oauth2ClientDTO.setClientId(registeredClient.getClientId());
        oauth2ClientDTO.setClientSecret(registeredClient.getClientSecret());
        oauth2ClientDTO.setScopes(registeredClient.getScopes());
        oauth2ClientDTO.setAuthorizationGrantTypes(registeredClient.getAuthorizationGrantTypes());
        oauth2ClientDTO.setRedirectUris(registeredClient.getRedirectUris());
        oauth2ClientDTO.setStatus(registeredClient.getStatus());
        oauth2ClientDTO.setAutoApprove(registeredClient.getAutoApprove());

        return success(oauth2ClientDTO);
    }
}
