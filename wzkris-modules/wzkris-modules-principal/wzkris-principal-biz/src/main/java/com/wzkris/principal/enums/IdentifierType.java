package com.wzkris.principal.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

/**
 *
 */
@AllArgsConstructor
public enum IdentifierType {

    WE_XCX("we_xcx"),

    WE_GZH("we_gzh"),

    WEIBO("weibo");

    private final String value;

    @JsonCreator
    @Nullable
    public static IdentifierType fromValue(String value) {
        for (IdentifierType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
