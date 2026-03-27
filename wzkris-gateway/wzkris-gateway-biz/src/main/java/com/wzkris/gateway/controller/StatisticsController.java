package com.wzkris.gateway.controller;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.gateway.domain.response.ApiCallDailySeriesResponse;
import com.wzkris.gateway.domain.response.PageViewDailySeriesResponse;
import com.wzkris.gateway.service.StatisticsService;
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
 * @author : wzkris
 * @version : V1.0.0
 * @description : 统计控制器
 * @date : 2025/1/15
 */
@Slf4j
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@CheckAdminPerms("gateway-mod:statistics:pvuv")
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * 获取页面PV及UV统计（日）
     */
    @GetMapping("/pageview/daily")
    public Result<PageViewDailySeriesResponse> getPageViewDaily(
            @RequestParam String authType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        String dateStr = date != null ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) :
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Result.ok(statisticsService.getDailyPageViewSeries(authType, dateStr));
    }

    /**
     * 获取 API 调用次数统计（日）
     */
    @GetMapping("/apicall/daily")
    public Result<ApiCallDailySeriesResponse> getApiCallDaily(
            @RequestParam String authType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        String dateStr = date != null ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) :
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Result.ok(statisticsService.getDailyApiCallSeries(authType, dateStr));
    }

}

