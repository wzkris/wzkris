package com.wzkris.common.weixin.handler;

import com.wzkris.common.core.model.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 *
 * @author wzkris
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnClass(RestControllerAdvice.class)
public class WeixinExceptionHandler {

    /**
     * 微信异常
     */
    @ExceptionHandler(WxErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleWxErrorException(WxErrorException ex, HttpServletRequest request) {
        log.error("请求地址'{} {}',发生异常", request.getMethod(), request.getRequestURI(), ex);
        return Result.apiRequestFail(ex.getError().getErrorMsg());
    }

}
