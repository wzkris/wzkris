package com.wzkris.common.core.enums;

import lombok.AllArgsConstructor;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 业务状态码，常用的部分直接用http状态码;  __连接的枚举为子状态
 * @date : 2023/3/3 8:46
 */
@AllArgsConstructor
public enum BizCode {
    FAIL(1, "Operate Fail"),
    OK(0, "Success"),
    BAD_REQUEST(400, "Bad Request"),
    // 401未认证
    UNAUTHORIZED(401, "Unauthorized"),
    // 401子状态 非法token
    INVALID_TOKEN(450, "Invalid token"),
    // 403禁止访问
    FORBID(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    BAD_METHOD(405, "Method Not Allowed"),
    // 412前置条件不满足
    PRECONDITION_FAILED(412, "Precondition Failed"),
    INTERNAL_ERROR(500, "Internal Server Error"),
    BAD_GATEWAY(502, "Bad Gateway"),
    // 服务不可用
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    // 远程调用异常
    RPC_INVOCATION(1001, "Rpc Error"),
    // 三方服务异常
    THIRD_SERVICE(3003, "Third Service Error");

    // 状态码
    private final int biz;
    // 状态码描述
    private final String desc;

    public int value() {
        return this.biz;
    }

    public String desc() {
        return this.desc;
    }


}
