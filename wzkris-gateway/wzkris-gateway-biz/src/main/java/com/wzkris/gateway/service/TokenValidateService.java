package com.wzkris.gateway.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface TokenValidateService {

    Authentication loadAuthenticationByRequest(HttpServletRequest request);

}
