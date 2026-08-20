package com.wzkris.gateway.repository;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.gateway.domain.ApiCallEventDO;
import com.wzkris.gateway.domain.ApiCallStatsDO;

import java.util.Map;

/**
 * 统计数据存储接口（端口）
 *
 * <p>写入侧基于事件模型（{@link ApiCallEventDO}），
 * 每个事件对应一条时序数据点，可以直接写入：
 * <ul>
 *   <li>时序数据库：InfluxDB measurement / TimescaleDB hypertable</li>
 *   <li>关系型数据库：普通 INSERT，查询时用 GROUP BY + COUNT/SUM 聚合</li>
 * </ul>
 *
 * <p>读取侧返回已聚合的 {@link ApiCallStatsDO} 或计数，与底层存储解耦：
 * SQL 实现可用窗口函数，TSDB 实现可用 flux/PromQL。
 *
 * @author wzkris
 */
public interface ApiCallRepository {

    // ======== 写入 ========

    /**
     * 记录一条 API 调用事件
     */
    void recordApiCallEvent(ApiCallEventDO event);

    // ======== 读取 ========

    /**
     * 查询指定小时的 API 调用聚合统计
     *
     * @param authType 认证类型
     * @param hour     格式 yyyy-MM-dd-HH
     * @return 聚合统计，不存在时返回 {@link ApiCallStatsDO#zero()}
     */
    ApiCallStatsDO getHourlyApiCallStats(AuthTypeEnum authType, String hour);

    /**
     * 查询滑动窗口内的实时 API 统计（用于 QPS/成功率/耗时监控）
     *
     * @param authType      认证类型
     * @param windowSeconds 滑动窗口秒数，建议 5-300
     * @return 窗口聚合统计
     */
    ApiCallStatsDO getRealtimeApiCallStats(AuthTypeEnum authType, int windowSeconds);

    /**
     * 查询指定日期按路径分组的 API 调用聚合统计
     *
     * <p>等价 SQL：
     * <pre>
     *   SELECT path, COUNT(*) AS total,
     *          SUM(CASE WHEN success THEN 1 ELSE 0 END) AS success,
     *          SUM(CASE WHEN NOT success THEN 1 ELSE 0 END) AS error
     *   FROM api_call_event
     *   WHERE auth_type = :authType AND DATE(timestamp) = :date
     *   GROUP BY path
     * </pre>
     *
     * @param authType 认证类型
     * @param date     格式 yyyy-MM-dd
     * @return path → 聚合统计的映射，无数据时返回空 Map
     */
    Map<String, ApiCallStatsDO> getDailyApiCallStatsByPath(AuthTypeEnum authType, String date);

}
