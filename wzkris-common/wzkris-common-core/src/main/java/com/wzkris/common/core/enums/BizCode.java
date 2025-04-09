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
    // 401未认证
    UNAUTHORIZED(401, "Unauthorized"),
    // 403禁止访问
    FORBID(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    BAD_METHOD(405, "Method Not Allowed"),
    // 412前置条件不满足
    PRECONDITION_FAILED(412, "Precondition Failed"),
    // 超出频次限制
    TOO_MANY_REQUESTS(429, "Too Many Requests"),
    INTERNAL_ERROR(500, "Internal Server Error"),
    BAD_GATEWAY(502, "Bad Gateway"),
    // 服务不可用
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    // 调用失败
    INVOKE_FAIL(1000, "Invoke Fail"),
    // 远程调用异常
    RPC_ERROR(1100, "Rpc Error"),
    // 三方服务异常
    THIRD_SERVICE(3000, "Invoke Third Service Error");

    // 状态码
    private final int code;

    // 状态码描述
    private final String desc;

    public final int value() {
        return this.code;
    }

    public final String desc() {
        return this.desc;
    }

}
