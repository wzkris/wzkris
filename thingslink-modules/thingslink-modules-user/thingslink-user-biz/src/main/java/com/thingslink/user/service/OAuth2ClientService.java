package com.thingslink.user.service;

import com.thingslink.common.orm.page.Page;
import com.thingslink.user.domain.OAuth2Client;

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
