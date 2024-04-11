package com.thingslink.common.security.config.handler;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.enums.BizCode;
import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.security.utils.OAuth2ExceptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 认证失败处理类
 */
@Slf4j
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.info("token校验失败，请求URI：{}，详细信息：{}", request.getRequestURI(), authException.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        if (authException instanceof InvalidBearerTokenException invalidBearerTokenException) {
            Result<?> result = OAuth2ExceptionUtil.translate(invalidBearerTokenException.getError());
            JsonUtil.writeValue(response.getWriter(), result);
        }
        else {
            JsonUtil.writeValue(response.getWriter(), Result.resp(BizCode.UNAUTHORIZED));
        }
    }
}
