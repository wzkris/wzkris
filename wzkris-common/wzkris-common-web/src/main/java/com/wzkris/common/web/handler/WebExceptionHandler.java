package com.wzkris.common.web.handler;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.BusinessException;
import com.wzkris.common.core.exception.DemoModeException;
import com.wzkris.common.core.exception.ThirdServiceException;
import com.wzkris.common.core.exception.UtilException;
import com.wzkris.common.core.exception.param.CaptchaException;
import com.wzkris.common.core.exception.user.UserException;
import com.wzkris.common.core.utils.MessageUtil;
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

import static com.wzkris.common.core.domain.Result.fail;
import static com.wzkris.common.core.domain.Result.resp;

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
        log.info("请求地址'{} {}',不支持媒体类型，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return resp(BizCode.BAD_REQUEST, MessageUtil.message("request.media.error"));
    }

    /**
     * http请求数据格式异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.info("请求地址'{} {}',请求数据格式异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return resp(BizCode.BAD_REQUEST, MessageUtil.message("request.param.error"));
    }

    /**
     * 方法参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.error("请求地址'{} {}',捕获到方法入参异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return resp(BizCode.BAD_REQUEST, e.getMessage());
    }

    /**
     * 请求绑定异常
     */
    @ExceptionHandler(ServletRequestBindingException.class)
    public Result<?> handleNestedServletException(ServletRequestBindingException e, HttpServletRequest request) {
        log.info("请求地址'{} {}',捕获到请求绑定异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return resp(BizCode.BAD_REQUEST, e.getMessage());
    }

    /**
     * 文件异常
     */
    @ExceptionHandler(MultipartException.class)
    public Result<?> handleMultipartException(MultipartException e, HttpServletRequest request) {
        log.info("请求地址'{} {}',文件异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return resp(BizCode.BAD_REQUEST, e.getMessage());
    }

    /**
     * 404异常
     */
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public Result<?> handleNoHandlerFoundException(ServletException e, HttpServletRequest request) {
        log.info("请求地址'{} {}',404异常：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return resp(BizCode.NOT_FOUND);
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.info("请求地址'{}',不支持'{}'请求，异常信息：{}", request.getRequestURI(), e.getMethod(), e.getMessage());
        return resp(BizCode.BAD_METHOD, e.getMessage());
    }

    /**
     * 用户异常
     */
    @ExceptionHandler(UserException.class)
    public Result<?> handleUserException(UserException e, HttpServletRequest request) {
        log.info("请求地址'{} {}',捕获到用户异常,状态码：{},异常信息：{}", request.getMethod(), request.getRequestURI(), e.getBiz(), e.getMessage());
        return resp(e.getBiz(), null, e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.info("请求地址'{} {}',捕获到业务异常,业务状态码：{},异常信息：{}", request.getMethod(), request.getRequestURI(), e.getBiz(), e.getMessage());
        return resp(e.getBiz(), null, e.getMessage());
    }

    /**
     * 第三方服务异常
     */
    @ExceptionHandler(ThirdServiceException.class)
    public Result<?> handleThirdServiceException(ThirdServiceException e, HttpServletRequest request) {
        log.error("请求地址'{} {}',捕获到第三方服务异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return resp(e.getBiz(), null, e.getMessage());
    }

    /**
     * 验证码异常
     */
    @ExceptionHandler(CaptchaException.class)
    public Result<?> handledInvalidParamException(CaptchaException e, HttpServletRequest request) {
        log.info("请求地址'{} {}',捕获到验证码异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return resp(BizCode.PRECONDITION_FAILED, e.getMessage());
    }

    /**
     * 参数验证异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        log.info("请求地址'{} {}',捕获到参数验证异常，异常信息：{}", request.getMethod(), request.getRequestURI(), message);
        return resp(BizCode.PRECONDITION_FAILED, message);
    }

    /**
     * 参数验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> handleValidationException(ConstraintViolationException e, HttpServletRequest request) {
        ConstraintViolation violation = e.getConstraintViolations().toArray(new ConstraintViolation[0])[0];
        log.info("请求地址'{} {}',捕获到参数验证异常，异常信息：{}", request.getMethod(), request.getRequestURI(), violation.getMessage());
        return resp(BizCode.PRECONDITION_FAILED, violation.getMessage());
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public Result<?> handleDemoModeException() {
        return fail("DEMO_MODE");
    }

    /**
     * 工具异常
     */
    @ExceptionHandler(UtilException.class)
    public Result<?> handleUtilException(UtilException e, HttpServletRequest request) {
        log.info("请求地址'{} {}',捕获到工具异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return resp(BizCode.INTERNAL_ERROR, e.getMessage());
    }
}
