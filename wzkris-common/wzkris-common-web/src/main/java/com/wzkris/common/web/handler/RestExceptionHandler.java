package com.wzkris.common.web.handler;

import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.exception.BaseException;
import com.wzkris.common.core.model.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

/**
 * 统一将 Spring MVC 常见异常转换为 Result 返回体
 */
@Slf4j
@Order(100)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(exception = {
            BindException.class,
            IllegalArgumentException.class,
            ConstraintViolationException.class,
    })
    public ResponseEntity<Object> handleException(Exception ex, HttpServletRequest request) {
        log.error("请求地址'{} {}',发生异常", request.getMethod(), request.getRequestURI(), ex);

        if (ex instanceof BindException exception) {
            String message = exception.getFieldError().getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result.requestFail(message));
        } else if (ex instanceof IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result.requestFail(exception.getMessage()));
        } else if (ex instanceof ConstraintViolationException exception) {
            ConstraintViolation<?> violation = exception.getConstraintViolations().toArray(new ConstraintViolation[0])[0];
            String message = violation.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result.requestFail(message));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Result.requestFail(ex.getMessage()));
        }
    }

    // 处理父类中的 MethodArgumentNotValidException 异常
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Result.requestFail(errorMessage));
    }

    @ExceptionHandler(exception = {
            BaseException.class,
    })
    public ResponseEntity<Object> handleBaseException(BaseException ex, HttpServletRequest request) {
        log.error("{} 请求地址'{} {}',发生异常", ex.getModules(), request.getMethod(), request.getRequestURI(), ex);

        return ResponseEntity.status(ex.getHttpStatusCode())
                .body(Result.init(ex.getBiz(), null, ex.getMessage()));
    }

    /**
     * 重写此方法以统一处理MVC异常返回 Result
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            HttpServletRequest servletRequest = servletWebRequest.getRequest();
            log.error("请求地址'{} {}',发生mvc层异常", servletRequest.getMethod(), servletRequest.getRequestURI(), ex);
        }

        Result<?> resultBody = (body instanceof Result) ? (Result<?>) body
                : Result.init(BizBaseCode.API_REQUEST_ERROR.value(), null, ex.getMessage());
        return new ResponseEntity<>(resultBody, headers, statusCode);
    }

}


