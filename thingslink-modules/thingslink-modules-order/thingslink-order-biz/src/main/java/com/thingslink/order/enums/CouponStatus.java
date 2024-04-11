package com.thingslink.order.enums;

import lombok.AllArgsConstructor;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 优惠券状态
 * @date : 2023/2/8 11:08
 */
@AllArgsConstructor
public enum CouponStatus {
    OK("0", "通过审核，可以发放券"),
    WAIT("1", "等待审核"),
    STOP("2", "停止发放，但是券依然可以被用户使用"),
    FORBID("3", "停用，券无法发放也无法被使用");

    private final String code;
    private final String desc;

    public String value() {
        return this.code;
    }
}

