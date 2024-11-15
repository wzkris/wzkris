package com.wzkris.user.service;

import com.wzkris.user.domain.OAuth2Client;

import java.util.List;

/**
 * OAuth2服务
 *
 * @author wzkris
 */
public interface OAuth2ClientService {

    /**
     * 分页查询
     *
     * @param oAuth2Client 查询条件
     */
    List<OAuth2Client> list(OAuth2Client oAuth2Client);
}
