package com.wzkris.gateway.handler;

import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.exception.service.ResultException;
import com.wzkris.common.core.model.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 网关统一异常处理（Servlet / MVC 版本）
 *
 * @author wzkris
 */
@Slf4j
@RestControllerAdvice
public class GatewayExceptionHandler {

    @ExceptionHandler(ResultException.class)
    public ResponseEntity<Result<?>> handleResultException(ResultException ex, HttpServletRequest request) {
        log.error("[网关业务异常] 请求路径:'{} {}', 异常信息:{}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage(), ex);

        int status = ex.getHttpStatusCode();
        return ResponseEntity.status(status).body(ex.getResult());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<?>> handleException(Exception ex, HttpServletRequest request) {
        log.error("[网关系统异常] 请求路径:'{} {}', 异常信息:{}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.init(BizBaseCodeEnum.SYSTEM_ERROR.value(), null, ex.getMessage()));
    }

}
