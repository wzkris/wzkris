package com.wzkris.common.loadbalancer.enums;

import lombok.AllArgsConstructor;

/**
 * 路由策略枚举
 *
 * @author wzkris
 */
@AllArgsConstructor
public enum RoutePolicyEnum {
    /**
     * 关闭路由
     */
    CLOSE,

    /**
     * 开启路由
     */
    OPEN,

    /**
     * 强制路由
     */
    FORCE;

}