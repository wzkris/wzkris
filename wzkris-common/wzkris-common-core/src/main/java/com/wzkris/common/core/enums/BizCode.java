package com.wzkris.common.core.enums;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 业务状态码，常用的部分直接用http状态码;  __连接的枚举为子状态
 * @date : 2023/3/3 8:46
 */
@AllArgsConstructor
public enum BizCode {
    // 通用失败
    FAIL(1, "Operate Fail"),
    OK(0, "Success"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST),
    // 401未认证
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    // 401子状态 非法token
    UNAUTHORIZED__INVALID_TOKEN(401_02, "Invalid token"),
    // 403禁止访问
    FORBID(HttpStatus.FORBIDDEN),
    NOT_FOUND(HttpStatus.NOT_FOUND),
    BAD_METHOD(HttpStatus.METHOD_NOT_ALLOWED),
    // 412前置条件不满足
    PRECONDITION_FAILED(HttpStatus.PRECONDITION_FAILED),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY),
    // 服务不可用
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE),
    // 频繁重试
    FREQUENT_RETRY(912, "Frequent Retry"),
    // 限流
    LIMIT_FLOW(999, "Request Limit"),
    // 远程调用异常
    RPC_INVOCATION(1001, "Rpc Error"),
    // 三方服务异常
    THIRD_SERVICE(3003, "Third Service Error");

    // 状态码
    private final int code;
    // 状态码描述
    private final String desc;

    BizCode(HttpStatus httpStatus) {
        this.code = httpStatus.value();
        this.desc = httpStatus.getReasonPhrase();
    }

    public int value() {
        return this.code;
    }

    public String desc() {
        return this.desc;
    }


}
