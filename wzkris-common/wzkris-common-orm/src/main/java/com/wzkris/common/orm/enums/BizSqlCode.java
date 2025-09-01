package com.wzkris.common.orm.enums;

import lombok.AllArgsConstructor;

/**
 * 业务SQL状态码
 */
@AllArgsConstructor
public enum BizSqlCode {

    INJECT_SQL(20_001, "存在SQL注入");

    private final int code;

    private final String desc;

    public final int value() {
        return this.code;
    }

    public final String desc() {
        return this.desc;
    }

}
