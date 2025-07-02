package com.wzkris.common.openfeign.exception;

import lombok.Getter;

/**
 * 远程调用异常
 *
 * @author wzkris
 */
@Getter
public class RpcException extends RuntimeException {

    private final int code;

    public RpcException(int code, String message) {
        super(message);
        this.code = code;
    }

}
