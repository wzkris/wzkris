package com.thingslink.common.core.constant;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 安全常量
 * @date : 2023/11/17 9:13
 */
public interface SecurityConstants {
    /**
     * token请求头标识
     */
    String TOKEN_HEADER = "Authorization";

    /**
     * 个人信息
     */
    String PRINCIPAL_HEADER = "Current_Principal";

    /**
     * 内部请求路径
     */
    String INNER_REQUEST_PATH = "inner";

    /**
     * 角色前缀
     */
    String ROLE_PREFIX = "ROLE_";

    /**
     * 超级管理员ID
     */
    Long SUPER_ADMIN_ID = 1L;

    /**
     * 超级租户ID
     */
    Long SUPER_TENANT_ID = 0L;
}
