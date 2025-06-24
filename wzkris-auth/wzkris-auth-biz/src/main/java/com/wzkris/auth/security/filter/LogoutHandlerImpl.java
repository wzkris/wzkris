package com.wzkris.auth.security.filter;

import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.utils.StringUtil;
import jakarta.annotation.Nullable;
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
    public void logout(HttpServletRequest request, HttpServletResponse response, @Nullable Authentication authentication) {
        String accessToken = request.getHeader(HeaderConstants.X_TENANT_TOKEN);
        if (StringUtil.isNotBlank(accessToken)) {
            tokenService.logoutByAccessToken(accessToken);
        }
    }

}
