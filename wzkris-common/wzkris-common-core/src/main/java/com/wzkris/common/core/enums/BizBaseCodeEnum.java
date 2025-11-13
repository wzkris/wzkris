package com.wzkris.common.core.enums;

import lombok.AllArgsConstructor;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 业务状态码
 * @date : 2023/3/3 8:46
 */
@AllArgsConstructor
public enum BizBaseCodeEnum {
    OK(0, "ok"),
    ACCESS_DENIED(99_900, "禁止访问"),
    AUTHENTICATION_ERROR(99_901, "凭证异常"),
    REQUEST_ERROR(99_902, "请求异常"),
    API_REQUEST_ERROR(99_910, "api请求异常"),
    TOO_MANY_REQUESTS(99_998, "请求频率过多"),
    SYSTEM_ERROR(99_999, "系统异常");

    private final int code;

    private final String desc;

    public final int value() {
        return this.code;
    }

    public final String desc() {
        return this.desc;
    }
}
