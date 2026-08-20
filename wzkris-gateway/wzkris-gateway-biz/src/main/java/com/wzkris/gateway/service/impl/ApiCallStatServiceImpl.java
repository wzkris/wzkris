package com.wzkris.gateway.service.impl;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.gateway.domain.ApiCallDailyStatBundle;
import com.wzkris.gateway.domain.ApiCallRealtimeStatBundle;
import com.wzkris.gateway.domain.ApiCallStatsDO;
import com.wzkris.gateway.repository.ApiCallRepository;
import com.wzkris.gateway.service.ApiCallStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApiCallStatServiceImpl implements ApiCallStatService {

    private final ApiCallRepository apiCallRepository;

    @Override
    public ApiCallDailyStatBundle queryDaily(AuthTypeEnum authType, String date) {
        Map<String, ApiCallStatsDO> hours = new LinkedHashMap<>(24);
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
            hours.put(hourStr, stats);

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
        ApiCallStatsDO total = new ApiCallStatsDO(
                totalApi, totalSuccess, totalError,
                total2xx, total3xx, total4xx, total5xx,
                totalCostMs, avgCostMs, maxCostMs, methodMerged);

        return ApiCallDailyStatBundle.builder()
                .date(date)
                .total(total)
                .hours(hours)
                .paths(apiCallRepository.getDailyApiCallStatsByPath(authType, date))
                .build();
    }

    @Override
    public ApiCallRealtimeStatBundle queryRealtime(AuthTypeEnum authType, int windowSeconds) {
        int safeWindow = Math.max(5, Math.min(windowSeconds, 300));
        return ApiCallRealtimeStatBundle.builder()
                .windowSeconds(safeWindow)
                .stats(apiCallRepository.getRealtimeApiCallStats(authType, safeWindow))
                .build();
    }

}
