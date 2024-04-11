package com.thingslink.common.log.enums;

import lombok.AllArgsConstructor;

/**
 * 操作状态
 *
 * @author wzkris
 */
@AllArgsConstructor
public enum BusinessStatus {
    /**
     * 成功
     */
    SUCCESS("0"),

    /**
     * 失败
     */
    FAIL("1");

    private final String value;

    public String value() {
        return this.value;
    }

}
