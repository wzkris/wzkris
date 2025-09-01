package com.wzkris.common.openfeign.enums;

import lombok.AllArgsConstructor;

/**
 * rpc调用状态码
 */
@AllArgsConstructor
public enum BizRpcCode {

    RPC_ERROR(10_001, "Rpc调用失败，远程服务拒绝"),

    RPC_REMOTE_ERROR(10_002, "远程服务异常");

    private final int code;

    private final String desc;

    public final int value() {
        return this.code;
    }

    public final String desc() {
        return this.desc;
    }

}
