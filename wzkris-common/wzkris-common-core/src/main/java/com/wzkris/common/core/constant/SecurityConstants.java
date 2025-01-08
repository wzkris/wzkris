package com.wzkris.common.core.constant;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 安全常量
 * @date : 2023/11/17 9:13
 */
public interface SecurityConstants {

    /**
     * 内部请求路径
     */
    String INNER_REQUEST_PATH = "/inner";

    /**
     * 内部免鉴权请求路径
     */
    String INNER_NOAUTH_REQUEST_PATH = "/inner/noauth";

    /**
     * 超级管理员ID
     */
    Long SUPER_ADMIN_ID = 1L;

    /**
     * 超级租户ID
     */
    Long SUPER_TENANT_ID = 0L;
}
