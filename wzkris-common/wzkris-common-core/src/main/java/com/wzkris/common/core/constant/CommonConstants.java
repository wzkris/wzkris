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
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 不过期时间
     */
    public static final Date NEVER_EXPIRED_TIME = Date.from(Instant.parse("2099-12-31T23:59:59.999Z"));

    /**
     * 状态 启用
     */
    public static final String STATUS_ENABLE = "0";

    /**
     * 状态 禁用
     */
    public static final String STATUS_DISABLE = "1";

    /**
     * 成功
     */
    public static final String SUCCESS = "0";

    /**
     * 失败
     */
    public static final String FAIL = "1";

}
