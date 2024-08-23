package com.wzkris.common.security.handler;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.utils.MessageUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.wzkris.common.core.domain.Result.resp;

/**
 * 安全异常处理器
 *
 * @author wzkris
 */
@Slf4j
@RestControllerAdvice
public class SecurityExceptionHandler {

    /**
     * 401异常
     */
    @ExceptionHandler(AuthenticationException.class)
    public Result<?> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        log.info("请求地址'{} {}',401异常：{}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        return e.getMessage() == null ? resp(BizCode.UNAUTHORIZED) : resp(BizCode.UNAUTHORIZED, e.getMessage());
    }

    /**
     * 403异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<?> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.info("请求地址'{} {}',403异常：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return resp(BizCode.FORBID, MessageUtil.message("request.forbid"));
    }

}
