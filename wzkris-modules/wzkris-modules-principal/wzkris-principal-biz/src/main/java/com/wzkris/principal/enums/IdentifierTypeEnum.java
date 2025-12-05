package com.wzkris.principal.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 三方渠道类型
 */
@AllArgsConstructor
public enum IdentifierTypeEnum {

    WE_XCX("we_xcx"),

    WE_GZH("we_gzh"),

    WEIBO("weibo");

    private final String value;

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

    @JsonValue
    public String getValue() {
        return value;
    }

}
