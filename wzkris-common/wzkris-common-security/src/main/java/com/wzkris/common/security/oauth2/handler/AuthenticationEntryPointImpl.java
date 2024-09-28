package com.wzkris.common.security.oauth2.handler;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.utils.json.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 认证失败处理类
 */
@Slf4j
public final class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.info("token校验失败，请求URI：{}，详细信息：{}", request.getRequestURI(), exception.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        if (exception instanceof OAuth2AuthenticationException e) {
            if (e.getError() instanceof BearerTokenError bearerTokenError) {
                JsonUtil.writeValue(response.getWriter(),
                        Result.resp(bearerTokenError.getHttpStatus().value(), bearerTokenError.getErrorCode(), bearerTokenError.getDescription()));
            }
            else {
                JsonUtil.writeValue(response.getWriter(), Result.resp(BizCode.UNAUTHORIZED));
            }
        }
        else {
            JsonUtil.writeValue(response.getWriter(), Result.resp(BizCode.UNAUTHORIZED));
        }
    }
}
