package com.wzkris.common.openfeign.handler;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.openfeign.exception.RpcException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 远程异常处理器
 *
 * @author wzkris
 */
@Slf4j
@Order(-1)
@RestControllerAdvice
public class RpcExceptionHandler {

    @ExceptionHandler(RpcException.class)
    public void handleRpcException(RpcException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.error("请求地址'{} {}',发生远程调用异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        sendErrorResponse(response, e.getResult());
    }

    private void sendErrorResponse(HttpServletResponse response, Result<?> result) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        JsonUtil.writeValue(response.getWriter(), result);
    }

}
