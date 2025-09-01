package com.wzkris.common.core.enums;

import lombok.AllArgsConstructor;

/**
 * 业务调用服务状态码
 */
@AllArgsConstructor
public enum BizCaptchaCode {

    CAPTCHA_ERROR(40_101, "验证码错误"),

    CAPTCHA_EXPIRED(40_102, "验证码已过期");

    private final int code;

    private final String desc;

    public final int value() {
        return this.code;
    }

    public final String desc() {
        return this.desc;
    }

}
