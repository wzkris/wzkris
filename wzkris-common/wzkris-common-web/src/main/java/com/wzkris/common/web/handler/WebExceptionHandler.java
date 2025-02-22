package com.wzkris.common.web.handler;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.BaseException;
import com.wzkris.common.core.exception.mode.DemoModeException;
import com.wzkris.common.core.utils.I18nUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static com.wzkris.common.core.domain.Result.*;

/**
 * Web异常处理器
 *
 * @author wzkris
 */
@Slf4j
@RestControllerAdvice
public class WebExceptionHandler {

    /**
     * 不支持媒体类型
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result<?> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        log.error("请求地址'{} {}',不支持媒体类型，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return error400(I18nUtil.message("request.media.error"));
    }

    /**
     * http请求数据格式异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.error("请求地址'{} {}',请求数据格式异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return error400(I18nUtil.message("request.param.error"));
    }

    /**
     * 方法参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.error("请求地址'{} {}',捕获到方法入参异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return error400(e.getMessage());
    }

    /**
     * 请求绑定异常
     */
    @ExceptionHandler(ServletRequestBindingException.class)
    public Result<?> handleNestedServletException(ServletRequestBindingException e, HttpServletRequest request) {
        log.error("请求地址'{} {}',捕获到请求绑定异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return error400(e.getMessage());
    }

    /**
     * 文件异常
     */
    @ExceptionHandler(MultipartException.class)
    public Result<?> handleMultipartException(MultipartException e, HttpServletRequest request) {
        log.error("请求地址'{} {}',文件异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return error400(e.getMessage());
    }

    /**
     * 404异常
     */
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public Result<?> handleNoHandlerFoundException(ServletException e, HttpServletRequest request) {
        log.error("请求地址'{} {}',404异常：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return resp(BizCode.NOT_FOUND);
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("请求地址'{}',不支持'{}'请求，异常信息：{}", request.getRequestURI(), e.getMethod(), e.getMessage());
        return resp(BizCode.BAD_METHOD, e.getMessage());
    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(BaseException.class)
    public Result<?> handleBaseException(BaseException e, HttpServletRequest request) {
        log.error("请求地址'{} {}',异常模块：{}, 状态码：{}, 异常信息：{}",
                request.getMethod(), request.getRequestURI(), e.getModules(), e.getBiz(), e.getMessage());
        return resp(e.getBiz(), null, e.getMessage());
    }

    /**
     * 参数验证异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        log.error("请求地址'{} {}',捕获到参数验证异常，异常信息：{}", request.getMethod(), request.getRequestURI(), message);
        return error412(message);
    }

    /**
     * 参数验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> handleValidationException(ConstraintViolationException e, HttpServletRequest request) {
        ConstraintViolation violation = e.getConstraintViolations().toArray(new ConstraintViolation[0])[0];
        log.error("请求地址'{} {}',捕获到参数验证异常，异常信息：{}", request.getMethod(), request.getRequestURI(), violation.getMessage());
        String err = violation.getMessage();
        try {
            err = I18nUtil.messageRegex(err);
        } catch (Exception ignore) {
        }
        return error412(err);
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public Result<?> handleDemoModeException(DemoModeException e, HttpServletRequest request) {
        return error400(e.getMessage());
    }

    /**
     * 运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("请求地址'{} {}',捕获到运行时异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        if (e.getClass().getName().startsWith("java.lang")) { //JAVA异常则捕获，否则原样往外抛
            return error500(e.getMessage());
        }
        throw e;
    }
}
