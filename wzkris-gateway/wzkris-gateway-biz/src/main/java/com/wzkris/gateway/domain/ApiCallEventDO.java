package com.wzkris.gateway.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * API 调用时序事件（数据点，用于记录单次请求统计）
 *
 * <p>字段设计兼顾多种存储后端：
 * <ul>
 *   <li>时序数据库（InfluxDB/TimescaleDB）：timestamp为时间戳，authType/path/userId作为tag，success作为field</li>
 *   <li>关系型数据库：直接映射为api_call_event表的列，可按date_trunc/auth_type/path聚合</li>
 * </ul>
 *
 * @author wzkris
 */
@Data
@Builder
public class ApiCallEventDO {

    /**
     * 事件发生时间（精确到秒）
     */
    private LocalDateTime timestamp;

    /**
     * 认证类型（tag/维度），如 app / admin / miniapp
     */
    private String authType;

    /**
     * 请求路径（tag/维度）
     */
    private String path;

    /**
     * HTTP 方法
     */
    private String method;

    /**
     * 用户 ID（tag/维度，可为 null 表示匿名）
     */
    private Long userId;

    /**
     * 是否成功（2xx 视为成功）
     */
    private boolean success;

    /**
     * HTTP 状态码
     */
    private int statusCode;

    /**
     * 请求耗时（毫秒）
     */
    private long costMs;

}
