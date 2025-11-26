package com.wzkris.common.security.handler;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 安全异常处理器
 *
 * @author wzkris
 */
@Slf4j
@RestControllerAdvice
public class SecurityExceptionHandler {

    /**
     * OAuth2异常，自行抛出的时候会被这里拦截
     */
    @ExceptionHandler(OAuth2AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleAuthenticationException(OAuth2AuthenticationException ex, HttpServletRequest request) {
        log.error("请求地址'{} {}',发生OAuth2异常", request.getMethod(), request.getRequestURI(), ex);
        return OAuth2ExceptionUtil.translate(ex.getError());
    }

}
