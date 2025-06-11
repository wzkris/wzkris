package com.wzkris.common.openfeign.handler;

import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.openfeign.core.RmiFeign;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Feign调用异常处理器
 *
 * @author wzkris
 */
@Slf4j
@Order(-1)
@RestControllerAdvice(assignableTypes = RmiFeign.class)
public class FeignExceptionHandler {

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        String err = StringUtil.toString(e);
        log.error("远程feign调用地址'{} {}',发生异常：{}", request.getMethod(), request.getRequestURI(), err);
        response.setHeader(HeaderConstants.X_FEIGN_EXCEPTION, URLEncoder.encode(err, StandardCharsets.UTF_8));
    }

}
