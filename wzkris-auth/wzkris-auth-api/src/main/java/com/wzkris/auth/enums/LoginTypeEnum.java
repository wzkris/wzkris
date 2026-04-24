package com.wzkris.auth.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * @author wzkris
 * @date 2025/11/21
 * @description 登录类型枚举
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    REFRESH("refresh", "刷新模式"),
    PASSWORD("password", "密码模式"),
    SMS("sms", "短信模式"),
    WE_XCX("we_xcx", "微信小程序模式");

    @JsonValue
    private final String value;

    private final String description;

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

}
