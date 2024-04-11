package com.thingslink.common.security.config.handler;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

/**
 * @author wzkris
 * @description oauth2退出登录成功后的处理
 * @date 2024-04-10
 */
public class SsoLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final String REDIRECT_URL = "redirect_url";

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, @Nullable Authentication authentication) throws IOException, ServletException {
        if (response == null) {
            return;
        }

        // 获取请求参数中是否包含 回调地址
        String redirectUrl = request.getParameter(REDIRECT_URL);
        if (StrUtil.isNotBlank(redirectUrl)) {
            response.sendRedirect(redirectUrl);
        }
        else if (StrUtil.isNotBlank(request.getHeader(HttpHeaders.REFERER))) {
            // 默认跳转referer 地址
            String referer = request.getHeader(HttpHeaders.REFERER);
            response.sendRedirect(referer);
        }
    }
}
