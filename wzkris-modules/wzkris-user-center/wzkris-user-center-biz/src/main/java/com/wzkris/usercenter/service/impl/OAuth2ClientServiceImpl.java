package com.wzkris.usercenter.service.impl;

import com.wzkris.usercenter.mapper.OAuth2ClientMapper;
import com.wzkris.usercenter.service.OAuth2ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * OAuth2服务
 *
 * @author wzkris
 */
@Service
@AllArgsConstructor
public class OAuth2ClientServiceImpl implements OAuth2ClientService {

    private final OAuth2ClientMapper oauth2ClientMapper;

}
