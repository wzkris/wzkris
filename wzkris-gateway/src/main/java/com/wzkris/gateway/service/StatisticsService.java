package com.wzkris.gateway.service;

import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.gateway.domain.PvUv;
import com.wzkris.gateway.domain.PvUvSummary;
import com.wzkris.gateway.domain.vo.DailyStatisticsVO;
import com.wzkris.gateway.domain.vo.HourlyStatisticsVO;
import com.wzkris.gateway.domain.vo.PathStatisticsVO;
import com.wzkris.gateway.domain.vo.RealtimeStatisticsVO;
import com.wzkris.gateway.filter.route.StatisticsFilter.StatisticsKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBatch;
import org.redisson.api.RFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
     * 记录PV统计（优化：使用批量操作）
     */
    private void recordPV(StatisticsKey key, boolean success) {
        String date = key.getDate();
        String authType = key.getAuthType().getValue();
        String path = key.getPath();

        RBatch batch = RedisUtil.createBatch();

        // 按用户类型和路径统计PV
        String pvKey = String.format("%s:%s:%s:%s", PV_PREFIX, authType, date, path);
        batch.getAtomicLong(pvKey).incrementAndGetAsync();

        // 按用户类型统计总PV
        String totalPvKey = String.format("%s:total:%s:%s", PV_PREFIX, authType, date);
        batch.getAtomicLong(totalPvKey).incrementAndGetAsync();

        // 按日统计成功/失败（用于日汇总）
        String statusKey = String.format("%s:status:%s:%s:%s", PV_PREFIX, authType, date, success ? "success" : "error");
        batch.getAtomicLong(statusKey).incrementAndGetAsync();

        // 按路径统计成功/失败（用于路径统计）
        String pathStatusKey = String.format("%s:path:status:%s:%s:%s:%s", PV_PREFIX, authType, date, path, success ? "success" : "error");
        batch.getAtomicLong(pathStatusKey).incrementAndGetAsync();

        // 批量执行
        batch.execute();

        // 异步设置过期时间
        RedisUtil.expire(pvKey, Duration.ofDays(30));
        RedisUtil.expire(totalPvKey, Duration.ofDays(30));
        RedisUtil.expire(statusKey, Duration.ofDays(30));
        RedisUtil.expire(pathStatusKey, Duration.ofDays(30));
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
     * 记录日统计（优化：使用批量操作）
     */
    private void recordDailyStatistics(StatisticsKey key, boolean success) {
        String date = key.getDate();
        String authType = key.getAuthType().getValue();

        RBatch batch = RedisUtil.createBatch();

        // 日PV统计
        String dailyPvKey = String.format("%s:%s:%s", DAILY_PREFIX, authType, date);
        batch.getAtomicLong(dailyPvKey).incrementAndGetAsync();

        // 日成功/失败统计
        String dailyStatusKey = String.format("%s:status:%s:%s:%s", DAILY_PREFIX, authType, date, success ? "success" : "error");
        batch.getAtomicLong(dailyStatusKey).incrementAndGetAsync();

        // 批量执行
        batch.execute();

        // 异步设置过期时间
        RedisUtil.expire(dailyPvKey, Duration.ofDays(90));
        RedisUtil.expire(dailyStatusKey, Duration.ofDays(90));
    }

    /**
     * 记录小时统计（优化：使用批量操作）
     */
    private void recordHourlyStatistics(StatisticsKey key, boolean success) {
        String hour = key.getHour();
        String authType = key.getAuthType().getValue();
        Long userId = key.getUserId();

        RBatch batch = RedisUtil.createBatch();

        // 小时PV统计
        String hourlyPvKey = String.format("%s:%s:%s", HOURLY_PREFIX, authType, hour);
        batch.getAtomicLong(hourlyPvKey).incrementAndGetAsync();

        // 小时成功/失败统计
        String hourlyStatusKey = String.format("%s:status:%s:%s:%s", HOURLY_PREFIX, authType, hour, success ? "success" : "error");
        batch.getAtomicLong(hourlyStatusKey).incrementAndGetAsync();

        // 批量执行
        batch.execute();

        // 异步设置过期时间
        RedisUtil.expire(hourlyPvKey, Duration.ofDays(7));
        RedisUtil.expire(hourlyStatusKey, Duration.ofDays(7));

        // 小时UV统计（去重）- 使用Set，无法批量化
        if (userId != null) {
            String hourlyUvKey = String.format("%s:uv:%s:%s", HOURLY_PREFIX, authType, hour);
            String hourlyUserSetKey = String.format("%s:users:%s:%s", HOURLY_PREFIX, authType, hour);

            // 使用Set存储用户ID，实现去重
            RedisUtil.getRSet(hourlyUserSetKey).add(userId.toString());
            RedisUtil.expire(hourlyUserSetKey, Duration.ofDays(7));

            // 更新UV计数
            long uvCount = RedisUtil.getRSet(hourlyUserSetKey).size();
            RedisUtil.setObj(hourlyUvKey, uvCount, Duration.ofDays(7));
        }
    }

    /**
     * 获取PV统计
     */
    public long getPV(String authType, String date, String path) {
        String pvKey = String.format("%s:%s:%s:%s", PV_PREFIX, authType, date, path);
        return RedisUtil.getAtomicLong(pvKey).get();
    }

    /**
     * 获取UV统计
     */
    public long getUV(String authType, String date, String path) {
        String uvKey = String.format("%s:%s:%s:%s", UV_PREFIX, authType, date, path);
        return RedisUtil.getAtomicLong(uvKey).get();
    }

    /**
     * 获取日PV统计
     */
    public long getDailyPV(String authType, String date) {
        String dailyPvKey = String.format("%s:%s:%s", DAILY_PREFIX, authType, date);
        return RedisUtil.getAtomicLong(dailyPvKey).get();
    }

    /**
     * 获取日UV统计
     */
    public long getDailyUV(String authType, String date) {
        String totalUvKey = String.format("%s:total:%s:%s", UV_PREFIX, authType, date);
        return RedisUtil.getAtomicLong(totalUvKey).get();
    }

    /**
     * 获取小时PV统计
     */
    public long getHourlyPV(String authType, String hour) {
        String hourlyPvKey = String.format("%s:%s:%s", HOURLY_PREFIX, authType, hour);
        return RedisUtil.getAtomicLong(hourlyPvKey).get();
    }

    /**
     * 获取成功/失败统计
     */
    public long getStatusCount(String authType, String date, boolean success) {
        String statusKey = String.format("%s:status:%s:%s:%s", PV_PREFIX, authType, date, success ? "success" : "error");
        return RedisUtil.getAtomicLong(statusKey).get();
    }

    /**
     * 获取所有用户类型的统计（批量查询优化）
     */
    public PvUvSummary getDailySummary(String date) {
        String[] authTypes = {"user", "staff", "customer", "anonymous"};

        // 使用批量查询一次性获取所有数据
        Map<String, PvUv> dataMap = batchGetDailyStatistics(authTypes, date);

        return PvUvSummary.builder()
                .user(dataMap.get("user"))
                .staff(dataMap.get("staff"))
                .customer(dataMap.get("customer"))
                .anonymous(dataMap.get("anonymous"))
                .date(date)
                .build();
    }

    /**
     * 获取指定用户类型的日统计
     */
    public DailyStatisticsVO getDailyByAuthType(String authType, String date) {
        PvUv data = buildStatisticsData(authType, date);

        return DailyStatisticsVO.builder()
                .authType(authType)
                .date(date)
                .statistics(data)
                .build();
    }

    /**
     * 获取路径统计
     */
    public PathStatisticsVO getPathStatistics(String path, String date) {
        String[] authTypes = {"user", "staff", "customer", "anonymous"};

        // 使用批量查询
        Map<String, PvUv> dataMap = batchGetPathStatistics(authTypes, date, path);

        return PathStatisticsVO.builder()
                .path(path)
                .date(date)
                .stats(dataMap)
                .build();
    }

    /**
     * 获取小时统计
     */
    public HourlyStatisticsVO getHourlyStatistics(String authType, String date) {
        Map<String, PvUv> hourlyData = batchGetHourlyStatistics(authType, date);

        return HourlyStatisticsVO.builder()
                .authType(authType)
                .date(date)
                .hourlyData(hourlyData)
                .build();
    }

    /**
     * 获取实时统计
     */
    public RealtimeStatisticsVO getRealtimeStatistics() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String currentHour = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH"));
        String[] authTypes = {"user", "staff", "customer", "anonymous"};

        // 获取今日汇总
        PvUvSummary todaySummary = getDailySummary(date);

        // 获取当前小时统计
        Map<String, PvUv> currentHourStats = batchGetCurrentHourStatistics(authTypes, currentHour);

        return RealtimeStatisticsVO.builder()
                .timestamp(System.currentTimeMillis())
                .date(date)
                .currentHour(currentHourStats)
                .today(todaySummary)
                .build();
    }

    /**
     * 构建统计数据
     */
    private PvUv buildStatisticsData(String authType, String date) {
        return PvUv.builder()
                .pv(getDailyPV(authType, date))
                .uv(getDailyUV(authType, date))
                .successCount(getStatusCount(authType, date, true))
                .errorCount(getStatusCount(authType, date, false))
                .build();
    }

    /**
     * 批量获取日统计数据（优化：一次批量查询替代多次串行查询）
     */
    public Map<String, PvUv> batchGetDailyStatistics(String[] authTypes, String date) {
        RBatch batch = RedisUtil.createBatch();

        // 为每个authType准备4个查询：pv, uv, successCount, errorCount
        Map<String, Map<String, RFuture<Long>>> futuresMap = new HashMap<>();

        for (String authType : authTypes) {
            Map<String, RFuture<Long>> futures = new HashMap<>();

            String dailyPvKey = String.format("%s:%s:%s", DAILY_PREFIX, authType, date);
            String totalUvKey = String.format("%s:total:%s:%s", UV_PREFIX, authType, date);
            String successKey = String.format("%s:status:%s:%s:success", PV_PREFIX, authType, date);
            String errorKey = String.format("%s:status:%s:%s:error", PV_PREFIX, authType, date);

            futures.put("pv", batch.getAtomicLong(dailyPvKey).getAsync());
            futures.put("uv", batch.getAtomicLong(totalUvKey).getAsync());
            futures.put("success", batch.getAtomicLong(successKey).getAsync());
            futures.put("error", batch.getAtomicLong(errorKey).getAsync());

            futuresMap.put(authType, futures);
        }

        // 执行批量查询
        try {
            batch.execute();

            // 收集结果
            Map<String, PvUv> result = new HashMap<>();
            for (String authType : authTypes) {
                Map<String, RFuture<Long>> futures = futuresMap.get(authType);
                result.put(authType, PvUv.builder()
                        .pv(futures.get("pv").get())
                        .uv(futures.get("uv").get())
                        .successCount(futures.get("success").get())
                        .errorCount(futures.get("error").get())
                        .build());
            }
            return result;
        } catch (InterruptedException | ExecutionException e) {
            log.error("批量获取日统计数据失败", e);
            // 降级：逐个查询
            Map<String, PvUv> result = new HashMap<>();
            for (String authType : authTypes) {
                result.put(authType, buildStatisticsData(authType, date));
            }
            return result;
        }
    }

    /**
     * 批量获取路径统计数据（优化：一次批量查询，包含路径级别的成功/失败统计）
     */
    public Map<String, PvUv> batchGetPathStatistics(String[] authTypes, String date, String path) {
        RBatch batch = RedisUtil.createBatch();

        Map<String, Map<String, RFuture<Long>>> futuresMap = new HashMap<>();

        for (String authType : authTypes) {
            Map<String, RFuture<Long>> futures = new HashMap<>();

            String pvKey = String.format("%s:%s:%s:%s", PV_PREFIX, authType, date, path);
            String uvKey = String.format("%s:%s:%s:%s", UV_PREFIX, authType, date, path);
            String successKey = String.format("%s:path:status:%s:%s:%s:success", PV_PREFIX, authType, date, path);
            String errorKey = String.format("%s:path:status:%s:%s:%s:error", PV_PREFIX, authType, date, path);

            futures.put("pv", batch.getAtomicLong(pvKey).getAsync());
            futures.put("uv", batch.getAtomicLong(uvKey).getAsync());
            futures.put("success", batch.getAtomicLong(successKey).getAsync());
            futures.put("error", batch.getAtomicLong(errorKey).getAsync());

            futuresMap.put(authType, futures);
        }

        // 执行批量查询
        try {
            batch.execute();

            // 收集结果
            Map<String, PvUv> result = new HashMap<>();
            for (String authType : authTypes) {
                Map<String, RFuture<Long>> futures = futuresMap.get(authType);
                result.put(authType, PvUv.builder()
                        .pv(futures.get("pv").get())
                        .uv(futures.get("uv").get())
                        .successCount(futures.get("success").get())
                        .errorCount(futures.get("error").get())
                        .build());
            }
            return result;
        } catch (InterruptedException | ExecutionException e) {
            log.error("批量获取路径统计数据失败", e);
            // 降级：返回空数据
            Map<String, PvUv> result = new HashMap<>();
            for (String authType : authTypes) {
                result.put(authType, PvUv.builder().pv(0L).uv(0L).successCount(0L).errorCount(0L).build());
            }
            return result;
        }
    }

    /**
     * 批量获取小时统计数据（优化：一次批量查询24小时数据）
     */
    public Map<String, PvUv> batchGetHourlyStatistics(String authType, String date) {
        RBatch batch = RedisUtil.createBatch();

        Map<String, Map<String, RFuture<Long>>> futuresMap = new HashMap<>();

        // 为24小时准备查询
        for (int hour = 0; hour < 24; hour++) {
            String hourStr = String.format("%s-%02d", date, hour);
            String hourlyPvKey = String.format("%s:%s:%s", HOURLY_PREFIX, authType, hourStr);
            String hourlyUvKey = String.format("%s:uv:%s:%s", HOURLY_PREFIX, authType, hourStr);
            String successKey = String.format("%s:status:%s:%s:success", HOURLY_PREFIX, authType, hourStr);
            String errorKey = String.format("%s:status:%s:%s:error", HOURLY_PREFIX, authType, hourStr);
            String hourKey = String.format("%02d:00", hour);

            Map<String, RFuture<Long>> futures = new HashMap<>();
            futures.put("pv", batch.getAtomicLong(hourlyPvKey).getAsync());
            futures.put("uv", batch.getAtomicLong(hourlyUvKey).getAsync());
            futures.put("success", batch.getAtomicLong(successKey).getAsync());
            futures.put("error", batch.getAtomicLong(errorKey).getAsync());
            futuresMap.put(hourKey, futures);
        }

        // 执行批量查询
        try {
            batch.execute();

            // 收集结果
            Map<String, PvUv> result = new HashMap<>();
            for (Map.Entry<String, Map<String, RFuture<Long>>> entry : futuresMap.entrySet()) {
                Map<String, RFuture<Long>> futures = entry.getValue();
                result.put(entry.getKey(), PvUv.builder()
                        .pv(futures.get("pv").get())
                        .uv(futures.get("uv").get())
                        .successCount(futures.get("success").get())
                        .errorCount(futures.get("error").get())
                        .build());
            }
            return result;
        } catch (InterruptedException | ExecutionException e) {
            log.error("批量获取小时统计数据失败", e);
            // 降级：返回空数据
            Map<String, PvUv> result = new HashMap<>();
            for (int hour = 0; hour < 24; hour++) {
                result.put(String.format("%02d:00", hour), PvUv.builder()
                        .pv(0L)
                        .uv(0L)
                        .successCount(0L)
                        .errorCount(0L)
                        .build());
            }
            return result;
        }
    }

    /**
     * 批量获取当前小时统计（优化：一次批量查询）
     */
    public Map<String, PvUv> batchGetCurrentHourStatistics(String[] authTypes, String currentHour) {
        RBatch batch = RedisUtil.createBatch();

        Map<String, Map<String, RFuture<Long>>> futuresMap = new HashMap<>();

        for (String authType : authTypes) {
            String hourlyPvKey = String.format("%s:%s:%s", HOURLY_PREFIX, authType, currentHour);
            String hourlyUvKey = String.format("%s:uv:%s:%s", HOURLY_PREFIX, authType, currentHour);
            String successKey = String.format("%s:status:%s:%s:success", HOURLY_PREFIX, authType, currentHour);
            String errorKey = String.format("%s:status:%s:%s:error", HOURLY_PREFIX, authType, currentHour);

            Map<String, RFuture<Long>> futures = new HashMap<>();
            futures.put("pv", batch.getAtomicLong(hourlyPvKey).getAsync());
            futures.put("uv", batch.getAtomicLong(hourlyUvKey).getAsync());
            futures.put("success", batch.getAtomicLong(successKey).getAsync());
            futures.put("error", batch.getAtomicLong(errorKey).getAsync());
            futuresMap.put(authType, futures);
        }

        // 执行批量查询
        try {
            batch.execute();

            // 收集结果
            Map<String, PvUv> result = new HashMap<>();
            for (Map.Entry<String, Map<String, RFuture<Long>>> entry : futuresMap.entrySet()) {
                Map<String, RFuture<Long>> futures = entry.getValue();
                result.put(entry.getKey(), PvUv.builder()
                        .pv(futures.get("pv").get())
                        .uv(futures.get("uv").get())
                        .successCount(futures.get("success").get())
                        .errorCount(futures.get("error").get())
                        .build());
            }
            return result;
        } catch (InterruptedException | ExecutionException e) {
            log.error("批量获取当前小时统计数据失败", e);
            // 降级：返回空数据
            Map<String, PvUv> result = new HashMap<>();
            for (String authType : authTypes) {
                result.put(authType, PvUv.builder()
                        .pv(0L)
                        .uv(0L)
                        .successCount(0L)
                        .errorCount(0L)
                        .build());
            }
            return result;
        }
    }

}

