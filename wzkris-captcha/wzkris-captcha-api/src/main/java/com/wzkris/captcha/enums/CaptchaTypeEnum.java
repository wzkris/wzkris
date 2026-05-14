package com.wzkris.captcha.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码形态（网关 / 换票响应下发，前端据此选择组件与请求路径）。
 */
@Getter
@AllArgsConstructor
public enum CaptchaTypeEnum {

    CHALLENGE("CHALLENGE", "算力验证码"),

    SLIDE("SLIDE", "滑动验证码"),

    IMAGE("IMAGE", "图片验证码"),

    ;

    @JsonValue
    private final String value;

    private final String description;

    @JsonCreator
    public static CaptchaTypeEnum fromValue(String value) {
        String trimmed = value.trim();
        for (CaptchaTypeEnum typeEnum : values()) {
            if (typeEnum.value.equalsIgnoreCase(trimmed)) {
                return typeEnum;
            }
        }
        return null;
    }

}
