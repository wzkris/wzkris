package com.wzkris.auth.oauth2.filter;

import com.wzkris.auth.config.CaptchaConfig;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.common.core.utils.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author wzkris
 * @date 2024/7/8
 * @description 验证码过滤器
 */
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {

    private static final String CODE = "code";

    private static final String UUID = "uuid";

    private final CaptchaService captchaService;

    private final CaptchaConfig captchaConfig;

    public ValidateCodeFilter(CaptchaService captchaService, CaptchaConfig captchaConfig) {
        this.captchaService = captchaService;
        this.captchaConfig = captchaConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 验证码校验
        if (!captchaConfig.getEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 仅处理登录请求
        if (StringUtil.containsIgnoreCase(request.getRequestURI(), "/oauth2/token")) {
            // 刷新token请求，不处理
            String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
            if (StringUtil.containsIgnoreCase(grantType, OAuth2ParameterNames.REFRESH_TOKEN)) {
                filterChain.doFilter(request, response);
                return;
            }

            String code = request.getParameter(CODE);
            String uuid = request.getParameter(UUID);

            captchaService.validatePicCaptcha(uuid, code);
        }

    }
}
