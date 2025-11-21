package com.wzkris.auth.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * @author wzkris
 * @date 2025/11/21
 * @description 登录类型枚举
 */
@AllArgsConstructor
public enum LoginTypeEnum {
    /**
     * 刷新
     */
    REFRESH("refresh"),
    /**
     * 密码
     */
    PASSWORD("password"),
    /**
     * 短信
     */
    SMS("sms"),
    /**
     * 微信小程序
     */
    WE_XCX("we_xcx");

    private final String value;

    @JsonCreator
    @Nullable
    public static LoginTypeEnum fromValue(String value) {
        for (LoginTypeEnum typeEnum : values()) {
            if (typeEnum.value.equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
