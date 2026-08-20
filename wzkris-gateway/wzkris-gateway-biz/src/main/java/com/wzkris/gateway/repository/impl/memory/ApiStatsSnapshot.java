package com.wzkris.gateway.repository.impl.memory;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * API 统计桶快照
 */
@Getter
@AllArgsConstructor
public class ApiStatsSnapshot {

    private long total;

    private long success;

    private long error;

    private long status2xx;

    private long status3xx;

    private long status4xx;

    private long status5xx;

    private long totalCostMs;

    private long maxCostMs;

    private Map<String, Long> methodCounts;

}
