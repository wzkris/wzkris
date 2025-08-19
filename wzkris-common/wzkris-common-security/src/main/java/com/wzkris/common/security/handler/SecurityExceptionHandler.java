package com.wzkris.common.security.handler;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 安全异常处理器
 *
 * @author wzkris
 */
@Slf4j
@Component
@RestControllerAdvice
public class SecurityExceptionHandler {

    /**
     * OAuth2异常，自行抛出的时候会被这里拦截
     */
    @ExceptionHandler(OAuth2AuthenticationException.class)
    public Result<?> handleAuthenticationException(OAuth2AuthenticationException e, HttpServletRequest request) {
        return OAuth2ExceptionUtil.translate(e.getError());
    }

}
