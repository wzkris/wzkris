package com.wzkris.gateway.repository.impl.memory;

import com.wzkris.gateway.domain.ApiCallStatsDO;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * API 滑动窗口聚合器
 */
public class ApiStatsWindowAccumulator {

    private long total;

    private long success;

    private long error;

    private long status2xx;

    private long status3xx;

    private long status4xx;

    private long status5xx;

    private long totalCostMs;

    private long maxCostMs;

    private final Map<String, Long> methodCounts = new HashMap<>();

    public void merge(ApiStatsSnapshot snapshot) {
        total += snapshot.getTotal();
        success += snapshot.getSuccess();
        error += snapshot.getError();
        status2xx += snapshot.getStatus2xx();
        status3xx += snapshot.getStatus3xx();
        status4xx += snapshot.getStatus4xx();
        status5xx += snapshot.getStatus5xx();
        totalCostMs += snapshot.getTotalCostMs();
        maxCostMs = Math.max(maxCostMs, snapshot.getMaxCostMs());
        snapshot.getMethodCounts().forEach((k, v) -> methodCounts.merge(k, v, Long::sum));
    }

    public ApiCallStatsDO toStats() {
        long avgCost = total > 0 ? totalCostMs / total : 0L;
        return new ApiCallStatsDO(
                total,
                success,
                error,
                status2xx,
                status3xx,
                status4xx,
                status5xx,
                totalCostMs,
                avgCost,
                maxCostMs,
                methodCounts.isEmpty() ? Collections.emptyMap() : methodCounts
        );
    }

}
