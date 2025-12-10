package com.wzkris.common.captcha.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

/**
 * 存储类型
 *
 * @author wuhunyu
 * @date 2025/06/16 11:20
 **/
@AllArgsConstructor
public enum StoreTypeEnum {

    /**
     * 内存
     */
    IN_MEMORY("memory"),

    /**
     * redis
     */
    REDIS("redis"),

    /**
     * 自定义
     */
    CUSTOM("custom");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

}
