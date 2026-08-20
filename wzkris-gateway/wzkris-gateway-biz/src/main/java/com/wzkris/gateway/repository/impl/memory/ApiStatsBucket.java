package com.wzkris.gateway.repository.impl.memory;

import com.wzkris.gateway.domain.ApiCallStatsDO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * API 统计聚合桶
 */
public class ApiStatsBucket {

    private final LongAdder total = new LongAdder();

    private final LongAdder success = new LongAdder();

    private final LongAdder error = new LongAdder();

    private final LongAdder status2xx = new LongAdder();

    private final LongAdder status3xx = new LongAdder();

    private final LongAdder status4xx = new LongAdder();

    private final LongAdder status5xx = new LongAdder();

    private final LongAdder totalCostMs = new LongAdder();

    private final LongAdder maxCostMs = new LongAdder();

    private final ConcurrentHashMap<String, LongAdder> methodCounts = new ConcurrentHashMap<>();

    public void record(boolean isSuccess, int statusCode, long cost, String method) {
        total.increment();
        if (isSuccess) {
            success.increment();
        } else {
            error.increment();
        }

        if (statusCode >= 200 && statusCode < 300) {
            status2xx.increment();
        } else if (statusCode >= 300 && statusCode < 400) {
            status3xx.increment();
        } else if (statusCode >= 400 && statusCode < 500) {
            status4xx.increment();
        } else if (statusCode >= 500) {
            status5xx.increment();
        }

        long safeCost = Math.max(0L, cost);
        totalCostMs.add(safeCost);
        updateMax(maxCostMs, safeCost);

        String safeMethod = method == null || method.isBlank() ? "UNKNOWN" : method;
        methodCounts.computeIfAbsent(safeMethod, k -> new LongAdder()).increment();
    }

    public ApiCallStatsDO toStats() {
        long reqTotal = total.sum();
        long totalCost = totalCostMs.sum();
        long avgCost = reqTotal > 0 ? totalCost / reqTotal : 0L;
        Map<String, Long> methodSnapshot = methodCounts.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().sum()));

        return new ApiCallStatsDO(
                reqTotal,
                success.sum(),
                error.sum(),
                status2xx.sum(),
                status3xx.sum(),
                status4xx.sum(),
                status5xx.sum(),
                totalCost,
                avgCost,
                maxCostMs.sum(),
                methodSnapshot
        );
    }

    public ApiStatsSnapshot toSnapshot() {
        return new ApiStatsSnapshot(
                total.sum(),
                success.sum(),
                error.sum(),
                status2xx.sum(),
                status3xx.sum(),
                status4xx.sum(),
                status5xx.sum(),
                totalCostMs.sum(),
                maxCostMs.sum(),
                methodCounts.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().sum()))
        );
    }

    private void updateMax(LongAdder target, long value) {
        while (true) {
            long current = target.sum();
            if (value <= current) {
                return;
            }
            synchronized (target) {
                long afterLock = target.sum();
                if (value > afterLock) {
                    target.add(value - afterLock);
                    return;
                }
            }
        }
    }

}
