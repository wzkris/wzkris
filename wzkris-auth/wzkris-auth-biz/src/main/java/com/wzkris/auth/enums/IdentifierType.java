package com.wzkris.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IdentifierType {

    WX_XCX("wx_xcx"),

    WX_GZH("wx_gzh"),

    WEIBO("weibo");

    private final String value;
}
