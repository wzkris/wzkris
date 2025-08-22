package com.wzkris.common.core.enums;

import lombok.AllArgsConstructor;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 业务状态码
 * @date : 2023/3/3 8:46
 */
@AllArgsConstructor
public enum BizBaseCode {
    OK(0, "ok"),

    BAD_REQUEST(40000, "Bad Request"),

    UNAUTHORIZED(40001, "Unauthorized"),

    FORBID(40003, "Forbidden"),

    NOT_FOUND(40004, "Not Found"),

    BAD_METHOD(40005, "Method Not Allowed"),

    TOO_MANY_REQUESTS(40029, "Too Many Requests"),

    INTERNAL_ERROR(50000, "Internal Server Error"),

    BAD_GATEWAY(50002, "Bad Gateway"),

    SERVICE_UNAVAILABLE(50003, "Service Unavailable"),

    RPC_ERROR(11000, "Rpc Call Error, Remote Rejected"),

    RPC_REMOTE_ERROR(11001, "Remote Service Error"),

    THIRD_SERVICE_ERROR(30000, "Invoke Third Service Error");

    private final int code;

    private final String desc;

    public final int value() {
        return this.code;
    }

    public final String desc() {
        return this.desc;
    }
}
