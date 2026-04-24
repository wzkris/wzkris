package com.wzkris.usercenter.enums.social;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 三方渠道类型
 */
@Getter
@AllArgsConstructor
public enum IdentifierTypeEnum {

    WE_XCX("we_xcx", "微信小程序"),

    WE_GZH("we_gzh", "微信公众号"),

    WEIBO("weibo", "微博");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

    @JsonCreator
    @Nullable
    public static IdentifierTypeEnum fromValue(String value) {
        for (IdentifierTypeEnum typeEnum : values()) {
            if (typeEnum.value.equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }

}
