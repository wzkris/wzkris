package com.wzkris.common.core.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.BusinessException;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 响应信息部门，服务间调用使用
 *
 * @author wzkris
 */
@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class) // 驼峰转下划线
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -683617940437008912L;

    // 业务状态码
    private int biz;
    // 数据
    private T data;
    // 错误信息
    private String errMsg;
    // 当前时间戳
    private long timestamp;

    public Result() {
    }

    public Result(int biz, T data, String errMsg) {
        this.biz = biz;
        this.data = data;
        this.errMsg = errMsg;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> Result<T> ok() {
        return resp(BizCode.OK, BizCode.OK.desc());
    }

    public static <T> Result<T> ok(T data) {
        return resp(BizCode.OK.value(), data, BizCode.OK.desc());
    }

    public static <T> Result<T> fail() {
        return fail(BizCode.FAIL.desc());
    }

    public static <T> Result<T> fail(String errMsg) {
        return resp(BizCode.FAIL, errMsg);
    }

    public static <T> Result<T> resp(BizCode bizCode) {
        return resp(bizCode.value(), null, bizCode.desc());
    }

    public static <T> Result<T> resp(BizCode bizCode, String errMsg) {
        return resp(bizCode.value(), null, errMsg);
    }

    public static <T> Result<T> resp(int biz, T data, String errMsg) {
        return new Result<>(biz, data, errMsg);
    }

    public static <T> Result<T> toRes(int affectRows) {
        return affectRows > 0 ? ok() : fail();
    }

    public static <T> Result<T> toRes(boolean b) {
        return b ? ok() : fail();
    }

    /**
     * 是否成功
     */
    @JsonIgnore
    public boolean isSuccess() {
        return this.biz == BizCode.OK.value();
    }

    /**
     * 校验返回结果是否正常，若不是则抛出业务异常
     */
    @JsonIgnore
    public T checkData() throws BusinessException {
        if (this.isSuccess()) {
            return this.data;
        }
        throw new BusinessException(this.biz, this.errMsg);
    }
}
