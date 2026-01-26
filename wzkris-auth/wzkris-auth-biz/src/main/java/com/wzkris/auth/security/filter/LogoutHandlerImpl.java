package com.wzkris.auth.security.filter;

import com.wzkris.auth.listener.event.LogoutEvent;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.LoginUser;
import com.wzkris.common.core.utils.SpringUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.Serializable;

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

    /**
     * 由于该过滤器链未配置安全上下文解析，authentication必定为null
     *
     * @param request        the HTTP request
     * @param response       the HTTP response
     * @param authentication the current loginUser details
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, @Nullable Authentication authentication) {
        if (authentication == null) return;

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        LoginUser loginUser = (LoginUser) authenticationToken.getPrincipal();
        Serializable uid = tokenService.logoutByAccessToken(loginUser.getAuthType().getValue(), authenticationToken.getCredentials().toString());
        if (uid != null) {
            SpringUtil.getContext().publishEvent(new LogoutEvent(uid, loginUser.getAuthType()));
        }
    }

}
