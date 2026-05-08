package com.wzkris.gateway.domain;

import com.wzkris.common.core.enums.AuthTypeEnum;

/**
 * 统计键，用于标识统计维度
 *
 * @author wzkris
 */
@lombok.Data
@lombok.Builder
public class ApiCallStatKey {

    /**
     * 认证类型，如app/admin/miniapp
     */
    private AuthTypeEnum authType;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 请求路径
     */
    private String path;

    /**
     * HTTP方法，如GET/POST
     */
    private String method;

    /**
     * HTTP状态码
     */
    private Integer statusCode;

    /**
     * 请求耗时（毫秒）
     */
    private Long costMs;

    /**
     * 日期，格式yyyy-MM-dd
     */
    private String date;

    /**
     * 小时，格式yyyy-MM-dd-HH
     */
    private String hour;

}
