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

    BAD_REQUEST(40_000, "请求失败"),

    UNAUTHORIZED(40_001, "请求未认证"),

    FORBID(40_003, "拒绝请求"),

    NOT_FOUND(40_004, "请求地址不存在"),

    BAD_METHOD(40_005, "不支持此方法"),

    TOO_MANY_REQUESTS(40_029, "请求频率过多"),

    MISSING_PARAMETER(40_099, "参数缺失"),

    INTERNAL_ERROR(50_000, "服务异常"),

    BAD_GATEWAY(50_002, "网关异常"),

    SERVICE_UNAVAILABLE(50_003, "服务不可用"),

    GATEWAY_TIMEOUT(50_004, "网关超时");

    private final int code;

    private final String desc;

    public final int value() {
        return this.code;
    }

    public final String desc() {
        return this.desc;
    }
}
