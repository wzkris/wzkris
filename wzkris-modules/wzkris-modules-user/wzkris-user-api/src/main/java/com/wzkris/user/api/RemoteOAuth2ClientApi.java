package com.wzkris.user.api;

import com.wzkris.user.api.domain.response.OAuth2ClientResp;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - OAuth2客户端
 * @date : 2024/7/3 14:37
 */
public interface RemoteOAuth2ClientApi {

    /**
     * 根据clientid查询客户端信息
     *
     * @param clientid clientid
     * @return oauth2客户端
     */
    OAuth2ClientResp getByClientId(String clientid);

}
