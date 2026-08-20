package com.wzkris.gateway.impl.apicall;

import com.wzkris.common.core.model.Result;
import com.wzkris.gateway.api.apicall.ApiCallInfoApi;
import com.wzkris.gateway.api.apicall.request.ApiCallDailyStatRequest;
import com.wzkris.gateway.api.apicall.request.ApiCallRealtimeStatRequest;
import com.wzkris.gateway.api.apicall.response.ApiCallDailySeriesResponse;
import com.wzkris.gateway.api.apicall.response.ApiCallRealtimeResponse;
import com.wzkris.gateway.api.apicall.response.ApiCallResponse;
import com.wzkris.gateway.domain.ApiCallDailyStatBundle;
import com.wzkris.gateway.domain.ApiCallRealtimeStatBundle;
import com.wzkris.gateway.domain.ApiCallStatsDO;
import com.wzkris.gateway.service.ApiCallStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApiCallInfoApiImpl implements ApiCallInfoApi {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final ApiCallStatService apiCallStatService;

    private static ApiCallDailySeriesResponse toDailyResponse(ApiCallDailyStatBundle bundle) {
        Map<String, ApiCallResponse> hours = new LinkedHashMap<>(bundle.hours().size());
        bundle.hours().forEach((hour, stats) -> hours.put(hour, toResponse(stats)));

        Map<String, ApiCallResponse> paths = new LinkedHashMap<>(bundle.paths().size());
        bundle.paths().forEach((path, stats) -> paths.put(path, toResponse(stats)));

        return ApiCallDailySeriesResponse.builder()
                .date(bundle.date())
                .total(toResponse(bundle.total()))
                .hours(hours)
                .paths(paths)
                .build();
    }

    private static ApiCallRealtimeResponse toRealtimeResponse(ApiCallRealtimeStatBundle bundle) {
        int window = bundle.windowSeconds();
        ApiCallStatsDO stats = bundle.stats();
        double qps = (double) stats.getTotal() / window;
        double successQps = (double) stats.getSuccess() / window;
        double errorQps = (double) stats.getError() / window;
        double successRate = stats.getTotal() > 0 ? (double) stats.getSuccess() / stats.getTotal() : 0D;

        return ApiCallRealtimeResponse.builder()
                .windowSeconds(window)
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

    private static ApiCallResponse toResponse(ApiCallStatsDO stats) {
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

    @Override
    public Result<ApiCallDailySeriesResponse> queryDaily(ApiCallDailyStatRequest request) {
        String date = request.getDate() != null
                ? request.getDate().format(DATE_FORMAT)
                : LocalDate.now().format(DATE_FORMAT);
        return Result.ok(toDailyResponse(apiCallStatService.queryDaily(request.getAuthType(), date)));
    }

    @Override
    public Result<ApiCallRealtimeResponse> queryRealtime(ApiCallRealtimeStatRequest request) {
        int windowSeconds = request.getWindowSeconds() != null ? request.getWindowSeconds() : 60;
        return Result.ok(toRealtimeResponse(
                apiCallStatService.queryRealtime(request.getAuthType(), windowSeconds)));
    }

}
