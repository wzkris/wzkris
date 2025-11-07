package com.wzkris.gateway.controller;

import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.model.Result;
import com.wzkris.gateway.domain.vo.ApiCallDailySeriesVO;
import com.wzkris.gateway.domain.vo.PageViewDailySeriesVO;
import com.wzkris.gateway.security.annotation.RequireAuth;
import com.wzkris.gateway.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
@RequireAuth(authType = AuthType.ADMIN, permissions = {"gateway-mod:statistics:pvuv"})
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * 获取页面PV及UV统计（日）
     */
    @GetMapping("/pageview/daily")
    public Mono<Result<PageViewDailySeriesVO>> getPageViewDaily(
            @RequestParam String authType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        String dateStr = date != null ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) :
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Mono.just(Result.ok(statisticsService.getDailyPageViewSeries(authType, dateStr)));
    }

    /**
     * 获取 API 调用次数统计（日）
     */
    @GetMapping("/apicall/daily")
    public Mono<Result<ApiCallDailySeriesVO>> getApiCallDaily(
            @RequestParam String authType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        String dateStr = date != null ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) :
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Mono.just(Result.ok(statisticsService.getDailyApiCallSeries(authType, dateStr)));
    }

}
