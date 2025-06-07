package com.wzkris.user.rmi;

import com.wzkris.user.rmi.domain.resp.OAuth2ClientResp;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - OAuth2客户端
 * @date : 2024/7/3 14:37
 */
public interface RmiOAuth2ClientService {

    /**
     * 根据clientid查询客户端信息
     *
     * @param clientid clientid
     * @return oauth2客户端
     */
    OAuth2ClientResp getByClientId(String clientid);
}
