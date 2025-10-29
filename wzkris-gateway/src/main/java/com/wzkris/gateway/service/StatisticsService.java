package com.wzkris.gateway.service;

import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.gateway.domain.PvUv;
import com.wzkris.gateway.domain.PvUvSummary;
import com.wzkris.gateway.domain.vo.DailyStatisticsVO;
import com.wzkris.gateway.domain.vo.HourlyStatisticsVO;
import com.wzkris.gateway.domain.vo.PathStatisticsVO;
import com.wzkris.gateway.domain.vo.RealtimeStatisticsVO;
import com.wzkris.gateway.filter.route.PvUvStatisticsFilter.StatisticsKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBatch;
import org.redisson.api.RFuture;
import org.redisson.api.RMap;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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

    private static final String KEY_DELIM = ":";        // Redis 键分隔符

    private static final String STATUS_SUCCESS = "success";

    private static final String STATUS_ERROR = "error";

    private static final String AUTH_ANONYMOUS = "anonymous";

    private static final String[] AUTH_TYPES
            = {AuthType.USER.getValue(), AuthType.STAFF.getValue(), AuthType.CUSTOMER.getValue(), AUTH_ANONYMOUS};

    // 统计键前缀（Hash/Set 聚合）
    private static final String STATS_PV_DAY = "statistics:pv:day:";                 // Hash fields: {auth}

    private static final String STATS_PV_DAY_STATUS = "statistics:pv:status:day:";   // Hash fields: {auth}:success/error

    private static final String STATS_PV_PATH_DAY = "statistics:pv:path:day:";       // Hash fields: {auth}:{path}

    private static final String STATS_PV_PATH_DAY_STATUS = "statistics:pv:pathstatus:day:"; // Hash fields: {auth}:{path}:success/error

    private static final String STATS_PV_HOUR = "statistics:pv:hour:";               // Hash fields: {auth}

    private static final String STATS_PV_HOUR_STATUS = "statistics:pv:status:hour:"; // Hash fields: {auth}:success/error

    private static final String STATS_UV_USERS_DAY = "statistics:uv:users:day:";     // Set key: statistics:uv:users:day:{date}:{auth}

    private static final String STATS_UV_USERS_PATH_DAY = "statistics:uv:users:path:day:"; // Set key: statistics:uv:users:path:day:{date}:{auth}:{path}

    private static final String STATS_UV_USERS_HOUR = "statistics:uv:users:hour:";   // Set key: statistics:uv:users:hour:{hour}:{auth}

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

        // 新版：按日PV（auth 维度）
        String dayPvHashKey = STATS_PV_DAY + date;
        incrementMapField(dayPvHashKey, authType, 1L);

        // 新版：按日路径PV（键包含 auth，field 为 path）
        String dayPathPvHashKey = STATS_PV_PATH_DAY + date + KEY_DELIM + authType;
        incrementMapField(dayPathPvHashKey, path, 1L);

        // 新版：按日成功/失败统计（键包含 auth，field 为状态）
        String dayStatusHashKey = STATS_PV_DAY_STATUS + date + KEY_DELIM + authType;
        incrementMapField(dayStatusHashKey, success ? STATUS_SUCCESS : STATUS_ERROR, 1L);

        // 新版：按日路径成功/失败统计（键包含 auth 与 path，field 为状态）
        String dayPathStatusHashKey = STATS_PV_PATH_DAY_STATUS + date + KEY_DELIM + authType + KEY_DELIM + path;
        incrementMapField(dayPathStatusHashKey, success ? STATUS_SUCCESS : STATUS_ERROR, 1L);

        // 新版：小时级 PV 与状态
        String hour = key.getHour();
        String hourPvHashKey = STATS_PV_HOUR + hour;
        incrementMapField(hourPvHashKey, authType, 1L);

        String hourStatusHashKey = STATS_PV_HOUR_STATUS + hour + KEY_DELIM + authType;
        incrementMapField(hourStatusHashKey, success ? STATUS_SUCCESS : STATUS_ERROR, 1L);

        // 过期策略：Hash 键级过期
        expireMapIfNeeded(dayPvHashKey, Duration.ofDays(90));
        expireMapIfNeeded(dayPathPvHashKey, Duration.ofDays(30));
        expireMapIfNeeded(dayStatusHashKey, Duration.ofDays(90));
        expireMapIfNeeded(dayPathStatusHashKey, Duration.ofDays(30));
        expireMapIfNeeded(hourPvHashKey, Duration.ofDays(7));
        expireMapIfNeeded(hourStatusHashKey, Duration.ofDays(7));
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

        // 新版：按用户类型（日级）UV 去重
        String dayUsersSetKey = STATS_UV_USERS_DAY + date + KEY_DELIM + authType;
        RedisUtil.getRSet(dayUsersSetKey).add(userId.toString());
        expireSetIfNeeded(dayUsersSetKey, Duration.ofDays(90));

        // 新版：按路径（日级）UV 去重
        String dayPathUsersSetKey = STATS_UV_USERS_PATH_DAY + date + KEY_DELIM + authType + KEY_DELIM + path;
        RedisUtil.getRSet(dayPathUsersSetKey).add(userId.toString());
        expireSetIfNeeded(dayPathUsersSetKey, Duration.ofDays(30));

        // 新版：小时级 UV 去重
        String hour = key.getHour();
        String hourUsersSetKey = STATS_UV_USERS_HOUR + hour + KEY_DELIM + authType;
        RedisUtil.getRSet(hourUsersSetKey).add(userId.toString());
        expireSetIfNeeded(hourUsersSetKey, Duration.ofDays(7));
    }

    /**
     * 获取UV统计
     */
    public long getUV(String authType, String date, String path) {
        String dayPathUsersSetKey = STATS_UV_USERS_PATH_DAY + date + KEY_DELIM + authType + KEY_DELIM + path;
        int size = RedisUtil.scard(dayPathUsersSetKey);
        return Math.max(size, 0);
    }

    /**
     * 获取日PV统计
     */
    public long getDailyPV(String authType, String date) {
        String dayPvHashKey = STATS_PV_DAY + date;
        Object val = RedisUtil.getRMap(dayPvHashKey, Long.class).get(authType);
        return toLong(val);
    }

    /**
     * 获取日UV统计
     */
    public long getDailyUV(String authType, String date) {
        String dayUsersSetKey = STATS_UV_USERS_DAY + date + KEY_DELIM + authType;
        int size = RedisUtil.scard(dayUsersSetKey);
        return Math.max(size, 0);
    }

    /**
     * 获取成功/失败统计
     */
    public long getStatusCount(String authType, String date, boolean success) {
        String dayStatusHashKey = STATS_PV_DAY_STATUS + date + KEY_DELIM + authType;
        Object val = RedisUtil.getRMap(dayStatusHashKey, Long.class).get(success ? STATUS_SUCCESS : STATUS_ERROR);
        return toLong(val);
    }

    /**
     * 获取所有用户类型的统计（批量查询优化）
     */
    public PvUvSummary getDailySummary(String date) {
        Map<String, PvUv> dataMap = batchGetDailyStatistics(AUTH_TYPES, date);

        return PvUvSummary.builder()
                .user(dataMap.get(AuthType.USER.getValue()))
                .staff(dataMap.get(AuthType.STAFF.getValue()))
                .customer(dataMap.get(AuthType.CUSTOMER.getValue()))
                .anonymous(dataMap.get(AUTH_ANONYMOUS))
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
        Map<String, PvUv> dataMap = batchGetPathStatistics(AUTH_TYPES, date, path);

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

        // 获取今日汇总
        PvUvSummary todaySummary = getDailySummary(date);

        // 获取当前小时统计
        Map<String, PvUv> currentHourStats = batchGetCurrentHourStatistics(AUTH_TYPES, currentHour);

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

        // 新：批量从 Hash 读取（UV 仍按日级 set 单独读取一次）
        Map<String, Map<String, RFuture<Object>>> futuresMap = new HashMap<>();

        for (String authType : authTypes) {
            Map<String, RFuture<Object>> futures = new HashMap<>();

            String dayPvHashKey = STATS_PV_DAY + date;

            futures.put("pv", batch.getMap(dayPvHashKey, new TypedJsonJacksonCodec(String.class, Long.class)).getAsync(authType));
            String dayStatusHashKey = STATS_PV_DAY_STATUS + date + KEY_DELIM + authType;
            // Set 大小 Redisson 暂无 batch size 方法，降级为非批量读取（执行后再单查）
            futures.put("success", batch.getMap(dayStatusHashKey, new TypedJsonJacksonCodec(String.class, Long.class)).getAsync(STATUS_SUCCESS));
            futures.put("error", batch.getMap(dayStatusHashKey, new TypedJsonJacksonCodec(String.class, Long.class)).getAsync(STATUS_ERROR));

            futuresMap.put(authType, futures);
        }

        // 执行批量查询
        try {
            batch.execute();

            // 收集结果
            Map<String, PvUv> result = new HashMap<>();
            for (String authType : authTypes) {
                Map<String, RFuture<Object>> futures = futuresMap.get(authType);
                long pv = toLong(futures.get("pv").get());
                long success = toLong(futures.get("success").get());
                long error = toLong(futures.get("error").get());
                // UV 需要单独读取 set cardinality
                long uv = getDailyUV(authType, date);
                result.put(authType, PvUv.builder()
                        .pv(pv)
                        .uv(uv)
                        .successCount(success)
                        .errorCount(error)
                        .build());
            }
            return result;
        } catch (Exception e) {
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

        Map<String, Map<String, RFuture<Object>>> futuresMap = new HashMap<>();

        for (String authType : authTypes) {
            String dayPathPvHashKey = STATS_PV_PATH_DAY + date + KEY_DELIM + authType;
            String dayPathStatusHashKey = STATS_PV_PATH_DAY_STATUS + date + KEY_DELIM + authType + KEY_DELIM + path;
            Map<String, RFuture<Object>> futures = new HashMap<>();

            futures.put("pv", batch.getMap(dayPathPvHashKey, new TypedJsonJacksonCodec(String.class, Long.class)).getAsync(path));
            futures.put("success", batch.getMap(dayPathStatusHashKey, new TypedJsonJacksonCodec(String.class, Long.class)).getAsync(STATUS_SUCCESS));
            futures.put("error", batch.getMap(dayPathStatusHashKey, new TypedJsonJacksonCodec(String.class, Long.class)).getAsync(STATUS_ERROR));

            futuresMap.put(authType, futures);
        }

        // 执行批量查询
        try {
            batch.execute();

            // 收集结果
            Map<String, PvUv> result = new HashMap<>();
            for (String authType : authTypes) {
                Map<String, RFuture<Object>> futures = futuresMap.get(authType);
                long pv = toLong(futures.get("pv").get());
                long success = toLong(futures.get("success").get());
                long error = toLong(futures.get("error").get());
                long uv = getUV(authType, date, path);
                result.put(authType, PvUv.builder()
                        .pv(pv)
                        .uv(uv)
                        .successCount(success)
                        .errorCount(error)
                        .build());
            }
            return result;
        } catch (Exception e) {
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

        Map<String, Map<String, RFuture<Object>>> futuresMap = new HashMap<>();
        Map<String, RFuture<Integer>> uvFutures = new HashMap<>();

        // 为24小时准备查询（Hash）
        for (int hour = 0; hour < 24; hour++) {
            String hourStr = String.format("%s-%02d", date, hour);
            String hourKey = String.format("%02d:00", hour);

            String hourPvHashKey = STATS_PV_HOUR + hourStr;
            String hourStatusHashKey = STATS_PV_HOUR_STATUS + hourStr + KEY_DELIM + authType;

            Map<String, RFuture<Object>> futures = new HashMap<>();
            futures.put("pv", batch.getMap(hourPvHashKey, new TypedJsonJacksonCodec(String.class, Long.class)).getAsync(authType));
            futures.put("success", batch.getMap(hourStatusHashKey, new TypedJsonJacksonCodec(String.class, Long.class)).getAsync(STATUS_SUCCESS));
            futures.put("error", batch.getMap(hourStatusHashKey, new TypedJsonJacksonCodec(String.class, Long.class)).getAsync(STATUS_ERROR));
            futuresMap.put(hourKey, futures);

            // UV：批量 pipeline 统计基数
            String hourUsersSetKey = STATS_UV_USERS_HOUR + hourStr + KEY_DELIM + authType;
            uvFutures.put(hourKey, batch.getSet(hourUsersSetKey).sizeAsync());
        }

        // 执行批量查询
        try {
            batch.execute();

            // 收集结果
            Map<String, PvUv> result = new HashMap<>();
            for (Map.Entry<String, Map<String, RFuture<Object>>> entry : futuresMap.entrySet()) {
                Map<String, RFuture<Object>> futures = entry.getValue();
                long pv = toLong(futures.get("pv").get());
                long success = toLong(futures.get("success").get());
                long error = toLong(futures.get("error").get());
                long uv = toLong(uvFutures.get(entry.getKey()).get());
                result.put(entry.getKey(), PvUv.builder()
                        .pv(pv)
                        .uv(uv)
                        .successCount(success)
                        .errorCount(error)
                        .build());
            }
            return result;
        } catch (Exception e) {
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

        Map<String, Map<String, RFuture<Object>>> futuresMap = new HashMap<>();
        Map<String, RFuture<Integer>> uvFutures = new HashMap<>();

        for (String authType : authTypes) {
            String hourPvHashKey = STATS_PV_HOUR + currentHour;
            String hourStatusHashKey = STATS_PV_HOUR_STATUS + currentHour + KEY_DELIM + authType;

            Map<String, RFuture<Object>> futures = new HashMap<>();
            futures.put("pv", batch.getMap(hourPvHashKey, new TypedJsonJacksonCodec(String.class, Long.class)).getAsync(authType));
            futures.put("success", batch.getMap(hourStatusHashKey, new TypedJsonJacksonCodec(String.class, Long.class)).getAsync(STATUS_SUCCESS));
            futures.put("error", batch.getMap(hourStatusHashKey, new TypedJsonJacksonCodec(String.class, Long.class)).getAsync(STATUS_ERROR));
            futuresMap.put(authType, futures);

            // UV：批量 pipeline 统计基数
            String hourUsersSetKey = STATS_UV_USERS_HOUR + currentHour + KEY_DELIM + authType;
            uvFutures.put(authType, batch.getSet(hourUsersSetKey).sizeAsync());
        }

        // 执行批量查询
        try {
            batch.execute();

            // 收集结果
            Map<String, PvUv> result = new HashMap<>();
            for (Map.Entry<String, Map<String, RFuture<Object>>> entry : futuresMap.entrySet()) {
                Map<String, RFuture<Object>> futures = entry.getValue();
                long pv = toLong(futures.get("pv").get());
                long success = toLong(futures.get("success").get());
                long error = toLong(futures.get("error").get());
                long uv = toLong(uvFutures.get(entry.getKey()).get());
                result.put(entry.getKey(), PvUv.builder()
                        .pv(pv)
                        .uv(uv)
                        .successCount(success)
                        .errorCount(error)
                        .build());
            }
            return result;
        } catch (Exception e) {
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

    private long toLong(Object value) {
        if (value == null) return 0L;
        if (value instanceof Number) return ((Number) value).longValue();
        return Long.parseLong(String.valueOf(value));
    }

    private void incrementMapField(String mapKey, String field, long delta) {
        RMap<String, Long> map = RedisUtil.getRMap(mapKey, Long.class);

        map.addAndGet(field, delta);
    }

    private void expireMapIfNeeded(String key, Duration duration) {
        RMap<?, ?> map = RedisUtil.getRMap(key);
        long ttl = map.remainTimeToLive();
        if (ttl <= 0) {
            map.expire(duration);
        }
    }

    private void expireSetIfNeeded(String key, Duration duration) {
        var set = RedisUtil.getRSet(key);
        long ttl = set.remainTimeToLive();
        if (ttl <= 0) {
            set.expire(duration);
        }
    }

}

