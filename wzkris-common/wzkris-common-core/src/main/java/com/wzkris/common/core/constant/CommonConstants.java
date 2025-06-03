package com.wzkris.common.core.constant;

import java.time.Instant;
import java.util.Date;

/**
 * 通用常量信息
 *
 * @author wzkris
 */
public class CommonConstants {

    /**
     * RMI 远程方法调用
     */
    public static final String LOOKUP_RMI = "rmi:";

    /**
     * LDAP 远程方法调用
     */
    public static final String LOOKUP_LDAP = "ldap:";

    /**
     * LDAPS 远程方法调用
     */
    public static final String LOOKUP_LDAPS = "ldaps:";

    /**
     * 动态租户ID
     */
    public static final String X_TENANT_ID = "X-Tenant-Id";

    /**
     * tracing_id
     */
    public static final String X_TRACING_ID = "X-Tracing-Id";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 是否为系统默认（是）
     */
    public static final String YES = "Y";

    /**
     * 是否为系统默认（否）
     */
    public static final String NO = "N";

    /**
     * 不过期时间
     */
    public static final Date NOT_EXPIRED_TIME = Date.from(Instant.parse("2099-12-31T23:59:59.999Z"));

    /**
     * 状态 启用
     */
    public static final String STATUS_ENABLE = "0";

    /**
     * 状态 禁用
     */
    public static final String STATUS_DISABLE = "1";
}
