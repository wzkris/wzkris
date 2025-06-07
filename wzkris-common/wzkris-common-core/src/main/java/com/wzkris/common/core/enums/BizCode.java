package com.wzkris.common.core.enums;

import lombok.AllArgsConstructor;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 业务状态码，常用的部分直接用http状态码;
 * @date : 2023/3/3 8:46
 */
@AllArgsConstructor
public enum BizCode {
    OK(0, "Success"),

    BAD_REQUEST(400, "Bad Request"),

    UNAUTHORIZED(401, "Unauthorized"),

    FORBID(403, "Forbidden"),

    NOT_FOUND(404, "Not Found"),

    BAD_METHOD(405, "Method Not Allowed"),

    PRECONDITION_FAILED(412, "Precondition Failed"),

    TOO_MANY_REQUESTS(429, "Too Many Requests"),

    INTERNAL_ERROR(500, "Internal Server Error"),

    BAD_GATEWAY(502, "Bad Gateway"),

    SERVICE_UNAVAILABLE(503, "Service Unavailable"),

    INVOKE_FAIL(1000, "Invoke Fail"),

    RPC_ERROR(1100, "Rpc Error"),

    DEPRECATED_API(1200, "Deprecated API"),

    VERSION_HIGH_API(1201, "API version too high"),

    THIRD_SERVICE_ERROR(3000, "Invoke Third Service Error");

    private final int code;

    private final String desc;

    public final int value() {
        return this.code;
    }

    public final String desc() {
        return this.desc;
    }
}
