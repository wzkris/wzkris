package com.wzkris.gateway.service.impl;

import com.wzkris.common.core.constant.JwtClaimConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.gateway.service.RiskPassJwtValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiskPassJwtValidateServiceImpl implements RiskPassJwtValidateService {

    private final JwtDecoder jwtDecoder;

    @Override
    public boolean isValid(String riskPassToken, String clientKey) {
        if (StringUtil.isBlank(riskPassToken) || StringUtil.isBlank(clientKey)) {
            return false;
        }
        try {
            Jwt jwt = jwtDecoder.decode(riskPassToken.trim());
            if (!JwtClaimConstants.TOKEN_TYPE_RISK_PASS.equals(
                    jwt.getClaimAsString(JwtClaimConstants.TOKEN_TYPE))) {
                return false;
            }
            return clientKey.equals(jwt.getSubject());
        } catch (JwtException ignored) {
            return false;
        }
    }

}
