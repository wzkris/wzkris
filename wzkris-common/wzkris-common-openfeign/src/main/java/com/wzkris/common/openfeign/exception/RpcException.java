package com.wzkris.common.openfeign.exception;

import com.wzkris.common.core.model.Result;
import lombok.Getter;

/**
 * 远程调用异常
 *
 * @author wzkris
 */
@Getter
public class RpcException extends RuntimeException {

    private final int httpStatusCode;

    private final Result result;

    public RpcException(int httpStatusCode, Result result) {
        super(result.getMessage());
        this.httpStatusCode = httpStatusCode;
        this.result = result;
    }

    public RpcException(int httpStatusCode, int biz, String msg) {
        super(msg);
        this.httpStatusCode = httpStatusCode;
        this.result = Result.resp(biz, null, msg);
    }

}
