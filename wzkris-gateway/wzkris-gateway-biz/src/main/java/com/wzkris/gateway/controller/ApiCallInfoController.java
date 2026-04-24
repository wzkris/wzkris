package com.wzkris.gateway.controller;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.gateway.domain.request.ApiCallDailyQueryRequest;
import com.wzkris.gateway.domain.request.ApiCallRealtimeQueryRequest;
import com.wzkris.gateway.domain.response.ApiCallDailySeriesResponse;
import com.wzkris.gateway.domain.response.ApiCallRealtimeResponse;
import com.wzkris.gateway.service.ApiCallStatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * API调用统计查询控制器
 *
 * @author wzkris
 */
@Slf4j
@RestController
@RequestMapping("/api-call-info")
@RequiredArgsConstructor
@CheckAdminPerms("gateway-mod:statistics:pvuv")
public class ApiCallInfoController {

    private final ApiCallStatService apiCallStatService;

    @GetMapping("/query-daily")
    public Result<ApiCallDailySeriesResponse> queryDaily(ApiCallDailyQueryRequest request) {
        String dateStr = request.getDate() != null ? request.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) :
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Result.ok(apiCallStatService.queryDailyApiCallSeries(request.getAuthType(), dateStr));
    }

    @GetMapping("/query-realtime")
    public Result<ApiCallRealtimeResponse> queryRealtime(ApiCallRealtimeQueryRequest request) {
        Integer windowSeconds = request.getWindowSeconds() != null ? request.getWindowSeconds() : 60;
        return Result.ok(apiCallStatService.queryRealtimeApiCallStats(request.getAuthType(), windowSeconds));
    }

}
