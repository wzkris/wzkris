package com.wzkris.gateway.controller;

import com.wzkris.common.core.model.Result;
import com.wzkris.gateway.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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
    public Result<Map<String, Object>> getDailyStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }

        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        StatisticsService.StatisticsSummary summary = statisticsService.getAllStatistics(dateStr);

        Map<String, Object> result = new HashMap<>();
        result.put("date", dateStr);
        result.put("user", summary.getUser());
        result.put("staff", summary.getStaff());
        result.put("customer", summary.getCustomer());
        result.put("anonymous", summary.getAnonymous());

        // 计算总计
        long totalPv = summary.getUser().getPv() + summary.getStaff().getPv() +
                summary.getCustomer().getPv() + summary.getAnonymous().getPv();
        long totalUv = summary.getUser().getUv() + summary.getStaff().getUv() +
                summary.getCustomer().getUv() + summary.getAnonymous().getUv();
        long totalSuccess = summary.getUser().getSuccessCount() + summary.getStaff().getSuccessCount() +
                summary.getCustomer().getSuccessCount() + summary.getAnonymous().getSuccessCount();
        long totalError = summary.getUser().getErrorCount() + summary.getStaff().getErrorCount() +
                summary.getCustomer().getErrorCount() + summary.getAnonymous().getErrorCount();

        Map<String, Object> total = new HashMap<>();
        total.put("pv", totalPv);
        total.put("uv", totalUv);
        total.put("successCount", totalSuccess);
        total.put("errorCount", totalError);
        total.put("successRate", totalPv > 0 ? (double) totalSuccess / totalPv * 100 : 0);

        result.put("total", total);

        return Result.ok(result);
    }

    /**
     * 获取指定用户类型的统计
     */
    @GetMapping("/daily/{authType}")
    public Result<Map<String, Object>> getDailyStatisticsByAuthType(
            @PathVariable String authType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }

        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Map<String, Object> result = new HashMap<>();
        result.put("authType", authType);
        result.put("date", dateStr);
        result.put("pv", statisticsService.getDailyPV(authType, dateStr));
        result.put("uv", statisticsService.getDailyUV(authType, dateStr));
        result.put("successCount", statisticsService.getStatusCount(authType, dateStr, true));
        result.put("errorCount", statisticsService.getStatusCount(authType, dateStr, false));

        long pv = statisticsService.getDailyPV(authType, dateStr);
        long successCount = statisticsService.getStatusCount(authType, dateStr, true);
        result.put("successRate", pv > 0 ? (double) successCount / pv * 100 : 0);

        return Result.ok(result);
    }

    /**
     * 获取指定路径的统计
     */
    @GetMapping("/path")
    public Result<Map<String, Object>> getPathStatistics(
            @RequestParam String path,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }

        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Map<String, Object> result = new HashMap<>();
        result.put("path", path);
        result.put("date", dateStr);

        // 按用户类型统计
        Map<String, Object> authTypeStats = new HashMap<>();
        for (String authType : new String[]{"user", "staff", "customer", "anonymous"}) {
            Map<String, Object> stats = new HashMap<>();
            stats.put("pv", statisticsService.getPV(authType, dateStr, path));
            stats.put("uv", statisticsService.getUV(authType, dateStr, path));
            authTypeStats.put(authType, stats);
        }

        result.put("stats", authTypeStats);

        return Result.ok(result);
    }

    /**
     * 获取小时统计
     */
    @GetMapping("/hourly")
    public Result<Map<String, Object>> getHourlyStatistics(
            @RequestParam String authType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }

        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Map<String, Object> result = new HashMap<>();
        result.put("authType", authType);
        result.put("date", dateStr);

        // 获取24小时的数据
        Map<String, Long> hourlyData = new HashMap<>();
        for (int hour = 0; hour < 24; hour++) {
            String hourStr = String.format("%s-%02d", dateStr, hour);
            long pv = statisticsService.getHourlyPV(authType, hourStr);
            hourlyData.put(String.format("%02d:00", hour), pv);
        }

        result.put("hourlyData", hourlyData);

        return Result.ok(result);
    }

    /**
     * 获取实时统计
     */
    @GetMapping("/realtime")
    public Result<Map<String, Object>> getRealtimeStatistics() {
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Map<String, Object> result = new HashMap<>();
        result.put("timestamp", System.currentTimeMillis());
        result.put("date", dateStr);

        // 获取当前小时的统计
        String currentHour = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH"));
        Map<String, Long> currentHourStats = new HashMap<>();
        for (String authType : new String[]{"user", "staff", "customer", "anonymous"}) {
            currentHourStats.put(authType, statisticsService.getHourlyPV(authType, currentHour));
        }

        result.put("currentHour", currentHourStats);

        // 获取今日汇总
        StatisticsService.StatisticsSummary summary = statisticsService.getAllStatistics(dateStr);
        result.put("today", summary);

        return Result.ok(result);
    }

}
