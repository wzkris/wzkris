package com.wzkris.gateway.service;

import com.wzkris.gateway.domain.ApiCallStatsDO;
import com.wzkris.gateway.repository.ApiCallRepository;
import com.wzkris.gateway.response.ApiCallDailySeriesResponse;
import com.wzkris.gateway.response.ApiCallRealtimeResponse;
import com.wzkris.gateway.response.ApiCallResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * API统计查询服务
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class ApiCallStatService {

    private final ApiCallRepository apiCallRepository;

    public ApiCallRealtimeResponse queryRealtimeApiCallStats(String authType, Integer windowSeconds) {
        int safeWindow = Math.max(5, Math.min(windowSeconds, 300));
        ApiCallStatsDO stats = apiCallRepository.getRealtimeApiCallStats(authType, safeWindow);
        double qps = (double) stats.getTotal() / safeWindow;
        double successQps = (double) stats.getSuccess() / safeWindow;
        double errorQps = (double) stats.getError() / safeWindow;
        double successRate = stats.getTotal() > 0 ? (double) stats.getSuccess() / stats.getTotal() : 0D;

        return ApiCallRealtimeResponse.builder()
                .windowSeconds(safeWindow)
                .requestCount(stats.getTotal())
                .qps(qps)
                .successQps(successQps)
                .errorQps(errorQps)
                .successRate(successRate)
                .avgCostMs(stats.getAvgCostMs())
                .maxCostMs(stats.getMaxCostMs())
                .status2xxCount(stats.getStatus2xx())
                .status3xxCount(stats.getStatus3xx())
                .status4xxCount(stats.getStatus4xx())
                .status5xxCount(stats.getStatus5xx())
                .methodCounts(stats.getMethodCounts())
                .build();
    }

    public ApiCallDailySeriesResponse queryDailyApiCallSeries(String authType, String date) {
        Map<String, ApiCallResponse> hoursMap = new LinkedHashMap<>(24);
        long totalApi = 0L;
        long totalSuccess = 0L;
        long totalError = 0L;
        long total2xx = 0L;
        long total3xx = 0L;
        long total4xx = 0L;
        long total5xx = 0L;
        long totalCostMs = 0L;
        long maxCostMs = 0L;
        Map<String, Long> methodMerged = new HashMap<>();

        for (int h = 0; h < 24; h++) {
            String hourStr = String.format("%s-%02d", date, h);
            ApiCallStatsDO stats = apiCallRepository.getHourlyApiCallStats(authType, hourStr);
            hoursMap.put(hourStr, toResponse(stats));

            totalApi += stats.getTotal();
            totalSuccess += stats.getSuccess();
            totalError += stats.getError();
            total2xx += stats.getStatus2xx();
            total3xx += stats.getStatus3xx();
            total4xx += stats.getStatus4xx();
            total5xx += stats.getStatus5xx();
            totalCostMs += stats.getTotalCostMs();
            maxCostMs = Math.max(maxCostMs, stats.getMaxCostMs());
            if (stats.getMethodCounts() != null) {
                stats.getMethodCounts().forEach((k, v) -> methodMerged.merge(k, v, Long::sum));
            }
        }

        long avgCostMs = totalApi > 0 ? totalCostMs / totalApi : 0L;
        ApiCallResponse total = ApiCallResponse.builder()
                .apiCallCount(totalApi)
                .successCount(totalSuccess)
                .errorCount(totalError)
                .status2xxCount(total2xx)
                .status3xxCount(total3xx)
                .status4xxCount(total4xx)
                .status5xxCount(total5xx)
                .totalCostMs(totalCostMs)
                .avgCostMs(avgCostMs)
                .maxCostMs(maxCostMs)
                .methodCounts(methodMerged)
                .build();

        Map<String, ApiCallStatsDO> pathStats = apiCallRepository.getDailyApiCallStatsByPath(authType, date);
        Map<String, ApiCallResponse> pathTotals = new LinkedHashMap<>(pathStats.size());
        pathStats.forEach((path, stats) -> pathTotals.put(path, toResponse(stats)));

        return ApiCallDailySeriesResponse.builder()
                .date(date)
                .total(total)
                .hours(hoursMap)
                .paths(pathTotals)
                .build();
    }

    private ApiCallResponse toResponse(ApiCallStatsDO stats) {
        return ApiCallResponse.builder()
                .apiCallCount(stats.getTotal())
                .successCount(stats.getSuccess())
                .errorCount(stats.getError())
                .status2xxCount(stats.getStatus2xx())
                .status3xxCount(stats.getStatus3xx())
                .status4xxCount(stats.getStatus4xx())
                .status5xxCount(stats.getStatus5xx())
                .totalCostMs(stats.getTotalCostMs())
                .avgCostMs(stats.getAvgCostMs())
                .maxCostMs(stats.getMaxCostMs())
                .methodCounts(stats.getMethodCounts())
                .build();
    }

}
