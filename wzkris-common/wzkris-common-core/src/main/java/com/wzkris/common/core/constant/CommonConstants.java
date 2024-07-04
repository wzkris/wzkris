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
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 定时任务白名单配置（仅允许访问的包名，如其他需要可以自行添加）
     */
    public static final String[] JOB_WHITELIST_STR = {"com.wzkris"};

    /**
     * 定时任务违规的字符
     */
    public static final String[] JOB_ERROR_STR = {"java.net.URL", "jakarta.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework", "org.apache", "com.wzkris.common.core.utils.file"};

    /**
     * 是否为系统默认（是）
     */
    public static final String YES = "Y";

    /**
     * 是否为系统默认（否）
     */
    public static final String NO = "N";

    /**
     * 唯一
     */
    public final static String UNIQUE = "1";

    /**
     * 不唯一
     */
    public final static String NOT_UNIQUE = "0";

    /**
     * 状态 启用
     */
    public final static String STATUS_ENABLE = "0";

    /**
     * 状态 禁用
     */
    public final static String STATUS_DISABLE = "1";
}
