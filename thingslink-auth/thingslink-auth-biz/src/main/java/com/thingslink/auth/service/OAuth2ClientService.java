package com.thingslink.auth.service;

import com.thingslink.auth.domain.OAuth2Client;
import com.thingslink.common.orm.page.Page;

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
    Page<OAuth2Client> listPage(OAuth2Client oAuth2Client);
}
