package com.wzkris.common.core.constant;

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
    public final static long NOT_EXPIRED_TIME = -1L;

    /**
     * 状态 启用
     */
    public final static String STATUS_ENABLE = "0";

    /**
     * 状态 禁用
     */
    public final static String STATUS_DISABLE = "1";
}
