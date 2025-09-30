package com.wzkris.common.apikey.enums;

import lombok.AllArgsConstructor;

/**
 * 业务签名状态码
 */
@AllArgsConstructor
public enum BizSignCode {

    SIGN_NOT_EXIST(40_600, "签名不存在"),
    SIGN_ERROR(40_601, "签名异常");

    private final int code;

    private final String desc;

    public final int value() {
        return this.code;
    }

    public final String desc() {
        return this.desc;
    }

}
