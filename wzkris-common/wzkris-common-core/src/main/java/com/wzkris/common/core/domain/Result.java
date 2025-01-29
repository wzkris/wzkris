package com.wzkris.common.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.BusinessException;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 响应信息
 *
 * @author wzkris
 */
@Getter
@ToString
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -683617940437008912L;

    /**
     * 业务状态码
     */
    private int code;

    /**
     * 数据
     */
    private T data;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 当前时间戳
     */
    private long timestamp;

    public Result() {
    }

    public Result(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> Result<T> ok() {
        return resp(BizCode.OK, BizCode.OK.desc());
    }

    public static <T> Result<T> ok(T data) {
        return resp(BizCode.OK.value(), data, BizCode.OK.desc());
    }

    public static <T> Result<T> error400(String message) {
        return resp(BizCode.BAD_REQUEST, message);
    }

    public static <T> Result<T> error412(String message) {
        return resp(BizCode.PRECONDITION_FAILED, message);
    }

    public static <T> Result<T> error500(String message) {
        return resp(BizCode.INTERNAL_ERROR, message);
    }

    public static <T> Result<T> INVOKE_FAIL() {
        return resp(BizCode.INVOKE_FAIL);
    }

    public static <T> Result<T> resp(BizCode bizCode) {
        return resp(bizCode.value(), null, bizCode.desc());
    }

    public static <T> Result<T> resp(BizCode bizCode, String message) {
        return resp(bizCode.value(), null, message);
    }

    public static <T> Result<T> resp(int code, T data, String message) {
        return new Result<>(code, data, message);
    }

    /**
     * 是否成功
     */
    @JsonIgnore
    public boolean isSuccess() {
        return this.code == BizCode.OK.value();
    }

    /**
     * 校验返回结果是否正常，若不是则抛出业务异常
     */
    @JsonIgnore
    public T checkData() throws BusinessException {
        if (this.isSuccess()) {
            return this.data;
        }
        throw new BusinessException(this.code, this.message);
    }
}
