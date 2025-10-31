package com.wzkris.common.core.model;

import com.wzkris.common.core.enums.BizBaseCode;
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
        return init(BizBaseCode.OK.value(), null, BizBaseCode.OK.desc());
    }

    public static <T> Result<T> ok(T data) {
        return init(BizBaseCode.OK.value(), data, BizBaseCode.OK.desc());
    }

    public static <T> Result<T> requestFail(String message) {
        return init(BizBaseCode.REQUEST_ERROR.value(), null, message);
    }

    public static <T> Result<T> unauth(String message) {
        return init(BizBaseCode.AUTHENTICATION_ERROR.value(), null, message);
    }

    public static <T> Result<T> accessDenied(String message) {
        return init(BizBaseCode.ACCESS_DENIED.value(), null, message);
    }

    public static <T> Result<T> systemError(String message) {
        return init(BizBaseCode.SYSTEM_ERROR.value(), null, message);
    }

    public static <T> Result<T> init(int code, T data, String message) {
        return new Result<>(code, data, message);
    }

    /**
     * 是否成功
     */
    public final boolean isSuccess() {
        return this.code == BizBaseCode.OK.value();
    }

}
