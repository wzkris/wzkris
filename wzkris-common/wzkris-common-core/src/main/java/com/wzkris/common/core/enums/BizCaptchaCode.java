package com.wzkris.common.core.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BizCaptchaCode {

    CAPTCHA_ERROR(40101, "验证码错误");

    private final int code;

    private final String desc;

    public final int value() {
        return this.code;
    }

    public final String desc() {
        return this.desc;
    }

}
