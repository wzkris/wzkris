package com.wzkris.gateway.controller;

import com.wzkris.common.core.model.Result;
import com.wzkris.gateway.domain.PvUvSummary;
import com.wzkris.gateway.domain.vo.DailyStatisticsVO;
import com.wzkris.gateway.domain.vo.HourlyStatisticsVO;
import com.wzkris.gateway.domain.vo.PathStatisticsVO;
import com.wzkris.gateway.domain.vo.RealtimeStatisticsVO;
import com.wzkris.gateway.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

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
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * 获取日统计汇总
     */
    @GetMapping("/daily")
    public Result<PvUvSummary> getDailyStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }

        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Result.ok(statisticsService.getDailySummary(dateStr));
    }

    /**
     * 获取指定用户类型的统计
     */
    @GetMapping("/daily/{authType}")
    public Result<DailyStatisticsVO> getDailyStatisticsByAuthType(
            @PathVariable String authType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }

        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Result.ok(statisticsService.getDailyByAuthType(authType, dateStr));
    }

    /**
     * 获取指定路径的统计
     */
    @GetMapping("/path")
    public Result<PathStatisticsVO> getPathStatistics(
            @RequestParam String path,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }

        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Result.ok(statisticsService.getPathStatistics(path, dateStr));
    }

    /**
     * 获取小时统计
     */
    @GetMapping("/hourly")
    public Result<HourlyStatisticsVO> getHourlyStatistics(
            @RequestParam String authType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }

        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Result.ok(statisticsService.getHourlyStatistics(authType, dateStr));
    }

    /**
     * 获取实时统计（优化：使用批量查询）
     */
    @GetMapping("/realtime")
    public Result<RealtimeStatisticsVO> getRealtimeStatistics() {
        return Result.ok(statisticsService.getRealtimeStatistics());
    }

}
