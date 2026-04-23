package com.wzkris.gateway.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Map;

/**
 * API 调用聚合统计聚合对象（未持久化，用于内存聚合）
 *
 * <p>对应关系数据库中 GROUP BY 聚合或时序数据库的 sum() 聚合结果。
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiCallStatsDO {

    /**
     * 总调用次数
     */
    private long total;

    /**
     * 成功次数（HTTP 2xx）
     */
    private long success;

    /**
     * 失败次数（非 2xx）
     */
    private long error;

    /**
     * 2xx 次数
     */
    private long status2xx;

    /**
     * 3xx 次数
     */
    private long status3xx;

    /**
     * 4xx 次数
     */
    private long status4xx;

    /**
     * 5xx 次数
     */
    private long status5xx;

    /**
     * 总耗时（ms）
     */
    private long totalCostMs;

    /**
     * 平均耗时（ms）
     */
    private long avgCostMs;

    /**
     * 最大耗时（ms）
     */
    private long maxCostMs;

    /**
     * 方法维度计数，如 GET/POST
     */
    private Map<String, Long> methodCounts;

    public static ApiCallStatsDO zero() {
        return new ApiCallStatsDO(0L, 0L, 0L,
                0L, 0L, 0L, 0L,
                0L, 0L, 0L,
                Collections.emptyMap());
    }

}
