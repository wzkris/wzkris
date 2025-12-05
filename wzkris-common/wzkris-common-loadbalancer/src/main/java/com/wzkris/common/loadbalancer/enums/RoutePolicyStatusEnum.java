package com.wzkris.common.loadbalancer.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 路由策略状态
 */
@AllArgsConstructor
public enum RoutePolicyStatusEnum {
    /**
     * 关闭路由
     */
    CLOSE("0"),
    /**
     * 开启路由
     */
    OPEN("1"),
    /**
     * 强制路由
     */
    FORCE("2");

    private final String value;

    @JsonCreator
    @Nullable
    public static RoutePolicyStatusEnum fromValue(String value) {
        for (RoutePolicyStatusEnum policyEnum : values()) {
            if (policyEnum.value.equals(value)) {
                return policyEnum;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}