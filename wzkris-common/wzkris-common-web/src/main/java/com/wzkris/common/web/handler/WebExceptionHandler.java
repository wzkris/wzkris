package com.wzkris.common.web.handler;

import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.exception.BaseException;
import com.wzkris.common.core.exception.mode.DemoModeException;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.I18nUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import static com.wzkris.common.core.model.Result.*;

/**
 * Web异常处理器
 *
 * @author wzkris
 */
@Slf4j
@Order(100)
@RestControllerAdvice
public class WebExceptionHandler {

    /**
     * 不支持媒体类型
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result<?> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException e, HttpServletRequest request, HttpServletResponse response) {
        log.error("请求地址'{} {}',不支持媒体类型，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        response.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        return err40000(I18nUtil.message("invalidParameter.mediaType.invalid"));
    }

    /**
     * http请求数据格式异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e, HttpServletRequest request, HttpServletResponse response) {
        log.error("请求地址'{} {}',请求数据格式异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        response.setStatus(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.value());
        return Result.resp(BizBaseCode.MISSING_PARAMETER.value(), null, I18nUtil.message("invalidParameter.param.invalid"));
    }

    /**
     * 方法参数类型不匹配
     */
    @ExceptionHandler(TypeMismatchException.class)
    public Result<?> handleTypeMismatchException(
            TypeMismatchException e, HttpServletRequest request, HttpServletResponse response) {
        log.error("请求地址'{} {}',捕获到方法入参异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return err40000(e.getMessage());
    }

    /**
     * 请求绑定异常
     */
    @ExceptionHandler(ServletRequestBindingException.class)
    public Result<?> handleNestedServletException(ServletRequestBindingException e, HttpServletRequest request, HttpServletResponse response) {
        log.error("请求地址'{} {}',捕获到请求绑定异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return err40000(e.getMessage());
    }

    /**
     * 文件异常
     */
    @ExceptionHandler(MultipartException.class)
    public Result<?> handleMultipartException(MultipartException e, HttpServletRequest request, HttpServletResponse response) {
        log.error("请求地址'{} {}',文件异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return err40000(e.getMessage());
    }

    /**
     * 404异常
     */
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public Result<?> handleNoHandlerFoundException(ServletException e, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("请求地址'{} {}',404异常：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        }
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return resp(BizBaseCode.NOT_FOUND.value(), null, BizBaseCode.NOT_FOUND.desc());
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("请求地址'{}',不支持'{}'请求，异常信息：{}", request.getRequestURI(), e.getMethod(), e.getMessage());
        }
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        return resp(BizBaseCode.BAD_METHOD.value(), null, e.getMessage());
    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(BaseException.class)
    public Result<?> handleBaseException(BaseException e, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug(
                    "请求地址'{} {}',异常模块：{}, 状态码：{}, 异常信息：{}",
                    request.getMethod(),
                    request.getRequestURI(),
                    e.getModules(),
                    e.getBiz(),
                    e.getMessage());
        }
        return resp(e.getBiz(), null, e.getMessage());
    }

    /**
     * 参数验证异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("请求地址'{} {}',捕获到参数验证异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        }
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return err40000(e.getMessage());
    }

    /**
     * 参数验证异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, HttpServletRequest request, HttpServletResponse response) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        if (log.isDebugEnabled()) {
            log.debug("请求地址'{} {}',捕获到参数验证异常，异常信息：{}", request.getMethod(), request.getRequestURI(), message);
        }
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return err40000(message);
    }

    /**
     * 参数验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> handleValidationException(ConstraintViolationException e, HttpServletRequest request, HttpServletResponse response) {
        ConstraintViolation violation = e.getConstraintViolations().toArray(new ConstraintViolation[0])[0];
        if (log.isDebugEnabled()) {
            log.debug("请求地址'{} {}',捕获到参数验证异常，异常信息：{}", request.getMethod(), request.getRequestURI(),
                    violation.getMessage());
        }
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        String err = violation.getMessage();
        try {
            err = I18nUtil.messageRegex(err);
        } catch (Exception ignore) {
        }
        return err40000(err);
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public Result<?> handleDemoModeException(DemoModeException e, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return err40003(e.getMessage());
    }

    /**
     * 主机解析异常
     */
    @ExceptionHandler(UnknownHostException.class)
    public Result<?> handleUnknownHostException(UnknownHostException e, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_GATEWAY.value());
        return Result.resp(BizBaseCode.BAD_GATEWAY.value(), null, e.getMessage());
    }

    /**
     * 连接异常
     */
    @ExceptionHandler(ConnectException.class)
    public Result<?> handleConnectException(ConnectException e, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        return Result.resp(BizBaseCode.SERVICE_UNAVAILABLE.value(), null, e.getMessage());
    }

    /**
     * 连接超时异常
     */
    @ExceptionHandler(SocketTimeoutException.class)
    public Result<?> handleSocketTimeoutException(SocketTimeoutException e, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpStatus.GATEWAY_TIMEOUT.value());
        return Result.resp(BizBaseCode.GATEWAY_TIMEOUT.value(), null, e.getMessage());
    }

}
