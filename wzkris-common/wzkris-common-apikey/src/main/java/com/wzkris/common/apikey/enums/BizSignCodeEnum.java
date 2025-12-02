package com.wzkris.common.apikey.enums;

import lombok.AllArgsConstructor;

/**
 * 业务签名状态码
 */
@AllArgsConstructor
public enum BizSignCodeEnum {

    SIGN_NOT_EXIST(40_600, "签名不存在"),

    SIGN_ERROR(40_601, "签名验证失败"),

    SECRET_ERROR(40_602, "签名密钥异常"),

    SIGN_HEADER_ERROR(40_603, "签名头异常");

    private final int code;

    private final String desc;

    public final int value() {
        return this.code;
    }

    public final String desc() {
        return this.desc;
    }

}
