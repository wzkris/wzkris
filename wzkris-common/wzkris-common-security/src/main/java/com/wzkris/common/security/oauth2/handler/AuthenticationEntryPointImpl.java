package com.wzkris.common.security.oauth2.handler;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 认证失败处理类
 */
@Slf4j
public final class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("token校验失败，请求URI：{}，详细信息：{}", request.getRequestURI(), exception.getMessage(), exception);
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        if (exception instanceof OAuth2AuthenticationException oAuth2AuthenticationException) {
            Result<?> result = OAuth2ExceptionUtil.translate(oAuth2AuthenticationException.getError());
            JsonUtil.writeValue(response.getWriter(), result);
        } else {
            int status = BizCode.UNAUTHORIZED.value();
            if (response.getStatus() != HttpServletResponse.SC_OK) {
                status = response.getStatus();
                response.setStatus(HttpServletResponse.SC_OK);
            }
            JsonUtil.writeValue(
                    response.getWriter(), Result.resp(status, null, exception.getLocalizedMessage()));
        }
    }

}
