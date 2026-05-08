package com.wzkris.auth.security.filter;

import com.wzkris.auth.domain.TokenClaims;
import com.wzkris.auth.event.LogoutEvent;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.utils.SpringUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * 退出登录处理器
 *
 * @author wzkris
 */
@Slf4j
@Component
public class LogoutHandlerImpl implements LogoutHandler {

    private final TokenService tokenService;

    public LogoutHandlerImpl(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * 退出登录处理
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
        BaseLoginUser loginUser = (BaseLoginUser) authenticationToken.getPrincipal();
        Long uid = loginUser.getUid();
        String accessToken = authenticationToken.getCredentials().toString();

        // 解析 accessToken 获取 sid
        TokenClaims claims = tokenService.parseJwt(accessToken);
        String sid = claims.getSid();

        // 移除会话
        tokenService.revoke(loginUser.getAuthType().getValue(), uid, sid);

        // 发布登出事件
        SpringUtil.getContext().publishEvent(new LogoutEvent(uid, loginUser.getAuthType()));
    }

}
