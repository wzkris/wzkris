package com.wzkris.common.core.enums;

import lombok.AllArgsConstructor;

/**
 * 业务调用服务状态码
 */
@AllArgsConstructor
public enum BizCallCode {

    WX_ERROR(30_001, "调用微信服务异常"),

    DINGTALK_ERROR(30_002, "调用钉钉服务异常");

    private final int code;

    private final String desc;

    public final int value() {
        return this.code;
    }

    public final String desc() {
        return this.desc;
    }

}
