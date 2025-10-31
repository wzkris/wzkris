package com.wzkris.common.openfeign.exception;

import com.wzkris.common.core.model.Result;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.Assert;

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
        Assert.isTrue(!HttpStatusCode.valueOf(httpStatusCode).is2xxSuccessful(), "httpStatusCode must not be 2xx");
        this.httpStatusCode = httpStatusCode;
        this.result = result;
    }

    public RpcException(int httpStatusCode, int biz, String msg) {
        super(msg);
        Assert.isTrue(!HttpStatusCode.valueOf(httpStatusCode).is2xxSuccessful(), "httpStatusCode must not be 2xx");
        this.httpStatusCode = httpStatusCode;
        this.result = Result.init(biz, null, msg);
    }

}
