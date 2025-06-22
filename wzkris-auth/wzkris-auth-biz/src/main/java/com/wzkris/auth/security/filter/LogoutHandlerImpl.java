package com.wzkris.auth.security.filter;

import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.domain.CorePrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * 退出登录
 *
 * @author wzkris
 */
public class LogoutHandlerImpl implements LogoutHandler {

    private final TokenService tokenService;

    public LogoutHandlerImpl(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof CorePrincipal principal) {
            String accessToken = request.getHeader(HeaderConstants.X_TENANT_TOKEN);
            tokenService.logoutByAccessToken(accessToken);
            tokenService.kickoutOnlineSessionByAccessToken(principal.getId(), accessToken);
        }
    }

}
