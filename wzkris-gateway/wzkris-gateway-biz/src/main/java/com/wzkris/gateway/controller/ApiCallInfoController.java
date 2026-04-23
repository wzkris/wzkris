package com.wzkris.gateway.controller;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.gateway.domain.response.ApiCallDailySeriesResponse;
import com.wzkris.gateway.domain.response.ApiCallRealtimeResponse;
import com.wzkris.gateway.service.ApiCallStatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public Result<ApiCallDailySeriesResponse> queryDaily(
            @RequestParam String authType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        String dateStr = date != null ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) :
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Result.ok(apiCallStatService.queryDailyApiCallSeries(authType, dateStr));
    }

    @GetMapping("/query-realtime")
    public Result<ApiCallRealtimeResponse> queryRealtime(
            @RequestParam String authType,
            @RequestParam(required = false, defaultValue = "60") Integer windowSeconds) {
        return Result.ok(apiCallStatService.queryRealtimeApiCallStats(authType, windowSeconds));
    }

}
