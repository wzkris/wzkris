package com.wzkris.user.service;

import com.wzkris.common.orm.page.Page;
import com.wzkris.user.domain.OAuth2Client;

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
