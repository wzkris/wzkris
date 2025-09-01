package com.wzkris.auth.enums;

import lombok.AllArgsConstructor;

/**
 * 业务登录状态码
 */
@AllArgsConstructor
public enum BizLoginCode {

    // 登录请求参数异常
    PARAMETER_ERROR(40_201, "登录参数异常"),
    LOGIN_TYPE_ERROR(40_202, "不支持此登录方式"),
    OAUTH_CLIENT_ERROR(40_203, "不支持此客户端"),

    // 登录用户异常
    USER_NOT_EXIST(40_230, "登录用户不存在"),
    AUTHENTICATION_NOT_EXIST(40_231, "登录信息不存在"),
    USER_DISABLED(40_232, "账户被禁用, 请联系管理员"),
    TENANT_DISABLED(40_233, "所属租户被禁用, 请联系管理员"),
    TENANT_EXPIRED(40_234, "所属租户已过期, 请联系管理员"),
    TENANT_PACKAGE_EXPIRED(40_235, "租户套餐已过期, 请联系管理员续费");

    private final int code;

    private final String desc;

    public final int value() {
        return this.code;
    }

    public final String desc() {
        return this.desc;
    }

}
