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
     * 退出登录处理
     * 注意：JWT 是无状态的，无法直接删除。如果需要强制登出，应该通过 refreshToken 登出
     * 这里仅发布登出事件，实际的 token 清理应该由客户端删除或等待过期
     *
     * @param request        the HTTP request
     * @param response       the HTTP response
     * @param authentication the current loginUser details
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, @Nullable Authentication authentication) {
        if (authentication == null) {
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        LoginUser loginUser = (LoginUser) authenticationToken.getPrincipal();

        // JWT 是无状态的，无法直接删除，仅发布登出事件
        // 如果需要强制登出所有会话，应该通过 refreshToken 调用 logoutByRefreshToken
        Serializable uid = loginUser.getUid();
        SpringUtil.getContext().publishEvent(new LogoutEvent(uid, loginUser.getAuthType()));
    }

}
