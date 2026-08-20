package com.wzkris.usercenter.service.impl;

import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.usercenter.domain.OAuth2ClientDO;
import com.wzkris.usercenter.mapper.OAuth2ClientMapper;
import com.wzkris.usercenter.service.OAuth2ClientService;
import org.springframework.stereotype.Service;

/**
 * OAuth2服务
 *
 * @author wzkris
 */
@Service
public class OAuth2ClientServiceImpl
        extends ServiceImplPlus<OAuth2ClientMapper, OAuth2ClientDO>
        implements OAuth2ClientService {

}
