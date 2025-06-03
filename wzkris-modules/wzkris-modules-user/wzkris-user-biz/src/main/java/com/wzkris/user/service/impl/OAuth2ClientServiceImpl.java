package com.wzkris.user.service.impl;

import com.wzkris.user.mapper.OAuth2ClientMapper;
import com.wzkris.user.service.OAuth2ClientService;
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
