package com.wzkris.common.security.handler;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public Result<?> handleAuthenticationException(OAuth2AuthenticationException e, HttpServletRequest request) {
        return OAuth2ExceptionUtil.translate(e.getError());
    }

    /**
     * RPC异常
     */
    @ExceptionHandler(RpcException.class)
    public Result<Void> handleDubboException(RpcException e) {
        log.error("RPC异常: {}", e.getMessage());
        return Result.resp(BizCode.RPC_ERROR, e.getMessage());
    }

}
