package com.wzkris.common.openfeign.handler;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.openfeign.exception.RpcException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Feign调用异常处理器
 *
 * @author wzkris
 */
@Slf4j
@Order(-1)
@Component
@RestControllerAdvice
public class RpcExceptionHandler {

    /**
     * RPC异常
     */
    @ExceptionHandler(RpcException.class)
    public Result<?> handleRpcException(RpcException e, HttpServletRequest request) {
        log.error("请求地址'{} {}',发生远程调用异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return Result.resp(e.getCode(), null, e.getMessage());
    }

}
