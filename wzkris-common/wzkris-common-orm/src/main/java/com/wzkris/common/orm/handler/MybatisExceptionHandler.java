package com.wzkris.common.orm.handler;

import com.wzkris.common.core.model.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.wzkris.common.core.model.Result.systemError;

/**
 * Mybatis异常处理器
 *
 * @author wzkris
 */
@Slf4j
@RestControllerAdvice
public class MybatisExceptionHandler {

    /**
     * 索引异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handledDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        log.error("请求地址'{} {}',捕获到唯一索引异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        return systemError("唯一索引异常");
    }

    /**
     * Mybatis系统异常 通用处理
     */
    @ExceptionHandler(MyBatisSystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleCannotFindDataSourceException(MyBatisSystemException e, HttpServletRequest request) {
        log.error("请求地址'{} {}',Mybatis异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        return systemError("未找到数据源，请联系管理员确认");
    }

    /**
     * 拦截sql异常
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleSqlException(DataAccessException e, HttpServletRequest request) {
        log.error("请求地址'{} {}',捕获到sql异常，异常信息：{}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        return systemError("sql异常，请联系管理员");
    }

}
