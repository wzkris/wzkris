package com.wzkris.common.openfeign.handler;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.openfeign.core.RmiFeign;
import com.wzkris.common.openfeign.enums.BizRpcCode;
import com.wzkris.common.openfeign.utils.RpcMsgUtil;
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
 * Feign客户端调用异常处理器
 *
 * @author wzkris
 */
@Slf4j
@Order(-1)
@RestControllerAdvice(assignableTypes = RmiFeign.class)
public class FeignExceptionHandler {

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String err = RpcMsgUtil.getDetailMsg(e);
        log.error("远程feign调用地址'{} {}',发生异常：{}", request.getMethod(), request.getRequestURI(), err);
        sendErrorResponse(response, Result.resp(BizRpcCode.RPC_REMOTE_ERROR.value(), null, err));
    }

    private void sendErrorResponse(HttpServletResponse response, Result<?> result) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        JsonUtil.writeValue(response.getWriter(), result);
    }

}
