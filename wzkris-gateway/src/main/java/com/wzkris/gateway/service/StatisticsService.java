package com.wzkris.gateway.service;

import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.gateway.filter.StatisticsFilter.StatisticsKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 统计服务
 * @date : 2025/1/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private static final String PV_PREFIX = "statistics:pv";

    private static final String UV_PREFIX = "statistics:uv";

    private static final String DAILY_PREFIX = "statistics:daily";

    private static final String HOURLY_PREFIX = "statistics:hourly";

    /**
     * 记录统计数据
     */
    @Async("statisticsExecutor")
    public void recordStatistics(StatisticsKey key, boolean success) {
        try {
            // 记录PV
            recordPV(key, success);

            // 记录UV（需要去重）
            recordUV(key);

            // 记录日统计
            recordDailyStatistics(key, success);

            // 记录小时统计
            recordHourlyStatistics(key, success);

        } catch (Exception e) {
            log.error("统计记录异常: {}", e.getMessage(), e);
        }
    }

    /**
     * 记录PV统计
     */
    private void recordPV(StatisticsKey key, boolean success) {
        String date = key.getDate();
        String authType = key.getAuthType().getValue();
        String path = key.getPath();

        // 按用户类型和路径统计PV
        String pvKey = String.format("%s:%s:%s:%s", PV_PREFIX, authType, date, path);
        long pvCount = RedisUtil.incr(pvKey);
        // 只在首次创建时设置过期时间
        if (pvCount == 1) {
            RedisUtil.expire(pvKey, Duration.ofDays(30)); // 保留30天
        }

        // 按用户类型统计总PV
        String totalPvKey = String.format("%s:total:%s:%s", PV_PREFIX, authType, date);
        long totalPvCount = RedisUtil.incr(totalPvKey);
        // 只在首次创建时设置过期时间
        if (totalPvCount == 1) {
            RedisUtil.expire(totalPvKey, Duration.ofDays(30));
        }

        // 按成功/失败统计
        String statusKey = String.format("%s:status:%s:%s:%s", PV_PREFIX, authType, date, success ? "success" : "error");
        long statusCount = RedisUtil.incr(statusKey);
        // 只在首次创建时设置过期时间
        if (statusCount == 1) {
            RedisUtil.expire(statusKey, Duration.ofDays(30));
        }
    }

    /**
     * 记录UV统计（去重）
     */
    private void recordUV(StatisticsKey key) {
        if (key.getUserId() == null) {
            return; // 匿名用户不统计UV
        }

        String date = key.getDate();
        String authType = key.getAuthType().getValue();
        String path = key.getPath();
        Long userId = key.getUserId();

        // 按用户类型和路径统计UV
        String uvKey = String.format("%s:%s:%s:%s", UV_PREFIX, authType, date, path);
        String userSetKey = String.format("%s:users:%s:%s:%s", UV_PREFIX, authType, date, path);

        // 使用Set存储用户ID，实现去重
        RedisUtil.getRSet(userSetKey).add(userId.toString());
        RedisUtil.expire(userSetKey, Duration.ofDays(30));

        // 更新UV计数
        long uvCount = RedisUtil.getRSet(userSetKey).size();
        RedisUtil.setObj(uvKey, uvCount, Duration.ofDays(30));

        // 按用户类型统计总UV
        String totalUvKey = String.format("%s:total:%s:%s", UV_PREFIX, authType, date);
        String totalUserSetKey = String.format("%s:users:total:%s:%s", UV_PREFIX, authType, date);

        RedisUtil.getRSet(totalUserSetKey).add(userId.toString());
        RedisUtil.expire(totalUserSetKey, Duration.ofDays(30));

        long totalUvCount = RedisUtil.getRSet(totalUserSetKey).size();
        RedisUtil.setObj(totalUvKey, totalUvCount, Duration.ofDays(30));
    }

    /**
     * 记录日统计
     */
    private void recordDailyStatistics(StatisticsKey key, boolean success) {
        String date = key.getDate();
        String authType = key.getAuthType().getValue();

        // 日PV统计
        String dailyPvKey = String.format("%s:%s:%s", DAILY_PREFIX, authType, date);
        long pvCount = RedisUtil.incr(dailyPvKey);
        // 只在首次创建时设置过期时间
        if (pvCount == 1) {
            RedisUtil.expire(dailyPvKey, Duration.ofDays(90)); // 保留90天
        }

        // 日成功/失败统计
        String dailyStatusKey = String.format("%s:status:%s:%s:%s", DAILY_PREFIX, authType, date, success ? "success" : "error");
        long statusCount = RedisUtil.incr(dailyStatusKey);
        // 只在首次创建时设置过期时间
        if (statusCount == 1) {
            RedisUtil.expire(dailyStatusKey, Duration.ofDays(90));
        }
    }

    /**
     * 记录小时统计
     */
    private void recordHourlyStatistics(StatisticsKey key, boolean success) {
        String hour = key.getHour();
        String authType = key.getAuthType().getValue();

        // 小时PV统计
        String hourlyPvKey = String.format("%s:%s:%s", HOURLY_PREFIX, authType, hour);
        long pvCount = RedisUtil.incr(hourlyPvKey);
        // 只在首次创建时设置过期时间
        if (pvCount == 1) {
            RedisUtil.expire(hourlyPvKey, Duration.ofDays(7)); // 保留7天
        }

        // 小时成功/失败统计
        String hourlyStatusKey = String.format("%s:status:%s:%s:%s", HOURLY_PREFIX, authType, hour, success ? "success" : "error");
        long statusCount = RedisUtil.incr(hourlyStatusKey);
        // 只在首次创建时设置过期时间
        if (statusCount == 1) {
            RedisUtil.expire(hourlyStatusKey, Duration.ofDays(7));
        }
    }

    /**
     * 获取PV统计
     */
    public long getPV(String authType, String date, String path) {
        String pvKey = String.format("%s:%s:%s:%s", PV_PREFIX, authType, date, path);
        return RedisUtil.getObjOrDefault(pvKey, 0L);
    }

    /**
     * 获取UV统计
     */
    public long getUV(String authType, String date, String path) {
        String uvKey = String.format("%s:%s:%s:%s", UV_PREFIX, authType, date, path);
        return RedisUtil.getObjOrDefault(uvKey, 0L);
    }

    /**
     * 获取日PV统计
     */
    public long getDailyPV(String authType, String date) {
        String dailyPvKey = String.format("%s:%s:%s", DAILY_PREFIX, authType, date);
        return RedisUtil.getObjOrDefault(dailyPvKey, 0L);
    }

    /**
     * 获取日UV统计
     */
    public long getDailyUV(String authType, String date) {
        String totalUvKey = String.format("%s:total:%s:%s", UV_PREFIX, authType, date);
        return RedisUtil.getObjOrDefault(totalUvKey, 0L);
    }

    /**
     * 获取小时PV统计
     */
    public long getHourlyPV(String authType, String hour) {
        String hourlyPvKey = String.format("%s:%s:%s", HOURLY_PREFIX, authType, hour);
        return RedisUtil.getObjOrDefault(hourlyPvKey, 0L);
    }

    /**
     * 获取成功/失败统计
     */
    public long getStatusCount(String authType, String date, boolean success) {
        String statusKey = String.format("%s:status:%s:%s:%s", PV_PREFIX, authType, date, success ? "success" : "error");
        return RedisUtil.getObjOrDefault(statusKey, 0L);
    }

    /**
     * 获取所有用户类型的统计
     */
    public StatisticsSummary getAllStatistics(String date) {
        return StatisticsSummary.builder()
                .user(StatisticsData.builder()
                        .pv(getDailyPV("user", date))
                        .uv(getDailyUV("user", date))
                        .successCount(getStatusCount("user", date, true))
                        .errorCount(getStatusCount("user", date, false))
                        .build())
                .staff(StatisticsData.builder()
                        .pv(getDailyPV("staff", date))
                        .uv(getDailyUV("staff", date))
                        .successCount(getStatusCount("staff", date, true))
                        .errorCount(getStatusCount("staff", date, false))
                        .build())
                .customer(StatisticsData.builder()
                        .pv(getDailyPV("customer", date))
                        .uv(getDailyUV("customer", date))
                        .successCount(getStatusCount("customer", date, true))
                        .errorCount(getStatusCount("customer", date, false))
                        .build())
                .anonymous(StatisticsData.builder()
                        .pv(getDailyPV("anonymous", date))
                        .uv(getDailyUV("anonymous", date))
                        .successCount(getStatusCount("anonymous", date, true))
                        .errorCount(getStatusCount("anonymous", date, false))
                        .build())
                .build();
    }

    /**
     * 统计数据
     */
    @lombok.Data
    @lombok.Builder
    public static class StatisticsData {

        private long pv;

        private long uv;

        private long successCount;

        private long errorCount;

    }

    /**
     * 统计汇总
     */
    @lombok.Data
    @lombok.Builder
    public static class StatisticsSummary {

        private StatisticsData user;

        private StatisticsData staff;

        private StatisticsData customer;

        private StatisticsData anonymous;

    }

}
