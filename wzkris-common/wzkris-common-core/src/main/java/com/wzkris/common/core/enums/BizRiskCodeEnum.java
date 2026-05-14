package com.wzkris.common.core.enums;

import lombok.AllArgsConstructor;

/**
 * 风控业务码
 */
@AllArgsConstructor
public enum BizRiskCodeEnum {

    REQUIRES_CAPTCHA_MODAL(409_001, "需要完成安全验证"),

    REQUIRES_BLOCK_MODAL(409_002, "访问受限"),

    ;

    private final int code;

    private final String desc;

    public int value() {
        return code;
    }

    public String desc() {
        return desc;
    }
}
