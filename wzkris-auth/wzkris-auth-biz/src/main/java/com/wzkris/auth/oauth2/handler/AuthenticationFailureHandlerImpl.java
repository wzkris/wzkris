package com.wzkris.auth.oauth2.handler;

import com.wzkris.auth.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.utils.json.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author wzkris
 * @description oauth2登录失败处理器
 * @date 2024-03-01
 */
@Slf4j
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     *                  request.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        log.info("登录失败，详细信息：{}", exception.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        if (exception instanceof OAuth2AuthenticationException oAuth2AuthenticationException) {
            Result<?> result = OAuth2ExceptionUtil.translate(oAuth2AuthenticationException.getError());
            JsonUtil.writeValue(response.getWriter(), result);
        }
        else {
            JsonUtil.writeValue(response.getWriter(), Result.resp(BizCode.UNAUTHORIZED, exception.getLocalizedMessage()));
        }
    }

}
