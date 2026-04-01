package com.wzkris.gateway.service;

import com.wzkris.gateway.domain.StatisticsKey;
import com.wzkris.gateway.domain.response.ApiCallDailySeriesResponse;
import com.wzkris.gateway.domain.response.ApiCallResponse;
import com.wzkris.gateway.domain.response.PageViewDailySeriesResponse;
import com.wzkris.gateway.domain.response.PageViewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

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

    // 统计键前缀（Hash/Set 聚合）
    // API调用统计相关
    private static final String STATS_API_PATH_CALL_DAY = "statistics:api:path:day:";       // Hash fields: {auth}:{path}

    private static final String STATS_API_PATH_CALL_DAY_STATUS = "statistics:api:pathstatus:day:"; // Hash fields: {auth}:{path}:success/error

    private static final String STATS_API_CALL_HOUR = "statistics:api:hour:";               // Hash fields: {auth}

    private static final String STATS_API_CALL_HOUR_STATUS = "statistics:api:status:hour:"; // Hash fields: {auth}:success/error

    private static final String STATS_UV_USERS_DAY = "statistics:uv:users:day:";     // Set key: statistics:uv:users:day:{date}:{auth}

    private static final String STATS_UV_USERS_PATH_DAY = "statistics:uv:users:path:day:"; // Set key: statistics:uv:users:path:day:{date}:{auth}:{path}

    private static final String STATS_UV_USERS_HOUR = "statistics:uv:users:hour:";   // Set key: statistics:uv:users:hour:{hour}:{auth}

    // 页面真实PV埋点专用key（仅供页面埋点/TrackController使用！）
    private static final String PV_STATS_HOUR = "statistics:pv:hour:";    // Hash fields: {auth}

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 供 TrackController 使用的复合埋点方法：包括页面PV（PageView）和UV（独立访客数）
     */
    public void recordUvPv(StatisticsKey key) {
        try {
            // 记录页面PV
            recordPV(key);
            // 记录UV
            recordUV(key);
        } catch (Exception e) {
            log.error("页面PV/UV埋点异常: {}", e.getMessage(), e);
        }
    }

    /**
     * 用于网关中过滤器统计的api
     */
    public void recordApiCallStatistics(StatisticsKey key, boolean success) {
        try {
            recordApiCall(key, success);
        } catch (Exception e) {
            log.error("API调用量统计异常: {}", e.getMessage(), e);
        }
    }

    /**
     * 记录UV统计（去重）
     */
    private void recordUV(StatisticsKey key) {
        if (key.getUserId() == null) {
            return;
        }

        String date = key.getDate();
        String authType = key.getAuthType();
        String path = key.getPath();
        Long userId = key.getUserId();

        // 新版：按用户类型（日级）UV 去重
        String dayUsersSetKey = STATS_UV_USERS_DAY + date + KEY_DELIM + authType;
        redisTemplate.opsForSet().add(dayUsersSetKey, userId.toString());
        expireSetIfNeeded(dayUsersSetKey, Duration.ofDays(90));

        // 新版：按路径（日级）UV 去重
        String dayPathUsersSetKey = STATS_UV_USERS_PATH_DAY + date + KEY_DELIM + authType + KEY_DELIM + path;
        redisTemplate.opsForSet().add(dayPathUsersSetKey, userId.toString());
        expireSetIfNeeded(dayPathUsersSetKey, Duration.ofDays(30));

        // 新版：小时级 UV 去重
        String hour = key.getHour();
        String hourUsersSetKey = STATS_UV_USERS_HOUR + hour + KEY_DELIM + authType;
        redisTemplate.opsForSet().add(hourUsersSetKey, userId.toString());
        expireSetIfNeeded(hourUsersSetKey, Duration.ofDays(7));
    }

    /**
     * 记录页面PV埋点，真实页面访问量
     * 仅供 TrackController 上报调用
     */
    public void recordPV(StatisticsKey key) {
        String authType = key.getAuthType();
        String hour = key.getHour();
        // 每小时PV计数
        String hourPvKey = PV_STATS_HOUR + hour;
        incrementMapField(hourPvKey, authType, 1);
        expireMapIfNeeded(hourPvKey, java.time.Duration.ofDays(7));
    }

    /**
     * 记录接口调用量统计（优化：使用批量操作）
     */
    private void recordApiCall(StatisticsKey key, boolean success) {
        String date = key.getDate();
        String authType = key.getAuthType();
        String path = key.getPath();

        // 按日路径API调用量（ZSET：member=path，score=apiCall），键包含 auth
        String dayApiPathCallZsetKey = STATS_API_PATH_CALL_DAY + date + KEY_DELIM + authType;
        redisTemplate.opsForZSet().incrementScore(dayApiPathCallZsetKey, path, 1D);

        // 按日路径成功/失败统计（键包含 auth 与 path，field 为状态）
        String dayPathStatusHashKey = STATS_API_PATH_CALL_DAY_STATUS + date + KEY_DELIM + authType + KEY_DELIM + path;
        incrementMapField(dayPathStatusHashKey, success ? STATUS_SUCCESS : STATUS_ERROR, 1);

        // 小时级 API 调用量与状态
        String hour = key.getHour();
        String hourApiCallHashKey = STATS_API_CALL_HOUR + hour;
        incrementMapField(hourApiCallHashKey, authType, 1);

        String hourStatusHashKey = STATS_API_CALL_HOUR_STATUS + hour + KEY_DELIM + authType;
        incrementMapField(hourStatusHashKey, success ? STATUS_SUCCESS : STATUS_ERROR, 1);

        // 过期策略
        expireZsetIfNeeded(dayApiPathCallZsetKey, Duration.ofDays(30));
        expireMapIfNeeded(dayPathStatusHashKey, Duration.ofDays(30));
        expireMapIfNeeded(hourApiCallHashKey, Duration.ofDays(7));
        expireMapIfNeeded(hourStatusHashKey, Duration.ofDays(7));
    }

    /**
     * 获取日PV埋点统计
     * 仅供页面统计调用
     */
    public int getDailyPV(String authType, String date) {
        int total = 0;
        for (int h = 0; h < 24; h++) {
            String hourStr = String.format("%s-%02d", date, h);
            String key = PV_STATS_HOUR + hourStr;
            Object value = redisTemplate.opsForHash().get(key, authType);
            if (value instanceof Number) {
                total += ((Number) value).intValue();
            }
        }
        return total;
    }

    /**
     * 获取日UV统计
     */
    public int getDailyUV(String authType, String date) {
        String dayUsersSetKey = STATS_UV_USERS_DAY + date + KEY_DELIM + authType;
        Long sizeLong = redisTemplate.opsForSet().size(dayUsersSetKey);
        int size = sizeLong != null ? sizeLong.intValue() : 0;
        return Math.max(size, 0);
    }

    /**
     * 获取小时PV埋点统计
     * 仅供页面统计调用
     */
    public int getHourlyPV(String authType, String hour) {
        String key = PV_STATS_HOUR + hour;
        Object value = redisTemplate.opsForHash().get(key, authType);
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }

    /**
     * 获取小时UV统计
     */
    public int getHourlyUV(String authType, String hour) {
        String hourUsersSetKey = STATS_UV_USERS_HOUR + hour + KEY_DELIM + authType;
        Long sizeLong = redisTemplate.opsForSet().size(hourUsersSetKey);
        int size = sizeLong != null ? sizeLong.intValue() : 0;
        return Math.max(size, 0);
    }

    private void incrementMapField(String mapKey, String field, int delta) {
        redisTemplate.opsForHash().increment(mapKey, field, delta);
    }

    private void expireMapIfNeeded(String key, Duration duration) {
        Long ttl = redisTemplate.getExpire(key);
        if (ttl == null || ttl <= 0) {
            redisTemplate.expire(key, duration);
        }
    }

    private void expireSetIfNeeded(String key, Duration duration) {
        Long ttl = redisTemplate.getExpire(key);
        if (ttl == null || ttl <= 0) {
            redisTemplate.expire(key, duration);
        }
    }

    private void expireZsetIfNeeded(String key, Duration duration) {
        Long ttl = redisTemplate.getExpire(key);
        if (ttl == null || ttl <= 0) {
            redisTemplate.expire(key, duration);
        }
    }

    /**
     * 获取页面PV/UV（日）含24小时序列
     */
    public PageViewDailySeriesResponse getDailyPageViewSeries(String authType, String date) {
        java.util.Map<String, PageViewResponse> hoursMap = new java.util.LinkedHashMap<>(24);

        java.util.List<String> hourStrList = new java.util.ArrayList<>(24);
        for (int h = 0; h < 24; h++) {
            String hourStr = String.format("%s-%02d", date, h);
            hourStrList.add(hourStr);
        }

        // 使用管道批量获取数据
        java.util.List<Object> results = redisTemplate.executePipelined(
                (RedisCallback<Void>) connection -> {
                    for (String hourStr : hourStrList) {
                        // 每小时PV取指定field
                        connection.hashCommands().hGet((PV_STATS_HOUR + hourStr).getBytes(), authType.getBytes());
                        // 每小时UV使用集合大小
                        connection.setCommands().sCard((STATS_UV_USERS_HOUR + hourStr + KEY_DELIM + authType).getBytes());
                    }
                    return null;
                });

        for (int i = 0; i < hourStrList.size(); i++) {
            String hourStr = hourStrList.get(i);
            Object pvObj = results.get(i * 2);
            int pv = pvObj instanceof Number ? ((Number) pvObj).intValue() : 0;
            Object uvObj = results.get(i * 2 + 1);
            int uv = uvObj instanceof Number ? ((Number) uvObj).intValue() : 0;
            hoursMap.put(hourStr, PageViewResponse.builder().pv(pv).uv(uv).build());
        }
        PageViewResponse total = PageViewResponse.builder()
                .pv(getDailyPV(authType, date))
                .uv(getDailyUV(authType, date))
                .build();
        return PageViewDailySeriesResponse.builder()
                .date(date)
                .total(total)
                .hours(hoursMap)
                .build();
    }

    /**
     * 获取API调用（日）含24小时序列
     */
    public ApiCallDailySeriesResponse getDailyApiCallSeries(String authType, String date) {
        java.util.Map<String, ApiCallResponse> hoursMap = new java.util.LinkedHashMap<>(24);

        java.util.List<String> hourStrList = new java.util.ArrayList<>(24);
        for (int h = 0; h < 24; h++) {
            String hourStr = String.format("%s-%02d", date, h);
            hourStrList.add(hourStr);
        }

        // 使用管道批量获取数据
        java.util.List<Object> results = redisTemplate.executePipelined(
                (RedisCallback<Void>) connection -> {
                    for (String hourStr : hourStrList) {
                        String hourKey = STATS_API_CALL_HOUR + hourStr;
                        String statusKey = STATS_API_CALL_HOUR_STATUS + hourStr + KEY_DELIM + authType;
                        connection.hashCommands().hGet(hourKey.getBytes(), authType.getBytes());
                        connection.hashCommands().hGet(statusKey.getBytes(), STATUS_SUCCESS.getBytes());
                        connection.hashCommands().hGet(statusKey.getBytes(), STATUS_ERROR.getBytes());
                    }
                    return null;
                });

        for (int i = 0; i < hourStrList.size(); i++) {
            String hourStr = hourStrList.get(i);
            Object apiCntObj = results.get(i * 3);
            int apiCnt = apiCntObj instanceof Number ? ((Number) apiCntObj).intValue() : 0;
            Object successObj = results.get(i * 3 + 1);
            int success = successObj instanceof Number ? ((Number) successObj).intValue() : 0;
            Object errorObj = results.get(i * 3 + 2);
            int error = errorObj instanceof Number ? ((Number) errorObj).intValue() : 0;
            hoursMap.put(hourStr, ApiCallResponse.builder()
                    .apiCallCount(apiCnt)
                    .successCount(success)
                    .errorCount(error)
                    .build());
        }
        int totalApiCnt = 0;
        int totalSuccess = 0;
        int totalError = 0;
        for (ApiCallResponse vo : hoursMap.values()) {
            if (vo != null) {
                totalApiCnt += vo.getApiCallCount();
                totalSuccess += vo.getSuccessCount();
                totalError += vo.getErrorCount();
            }
        }
        ApiCallResponse total = ApiCallResponse.builder()
                .apiCallCount(totalApiCnt)
                .successCount(totalSuccess)
                .errorCount(totalError)
                .build();

        // 按路径（日）总计：来自 ZSET + 状态HASH
        String zsetKey = STATS_API_PATH_CALL_DAY + date + KEY_DELIM + authType;
        Set<ZSetOperations.TypedTuple<Object>> entries = redisTemplate.opsForZSet().rangeWithScores(zsetKey, 0, -1);
        java.util.Map<String, ApiCallResponse> pathTotals = new java.util.LinkedHashMap<>();

        // 如果路径数量为0，直接返回空Map，避免不必要的批量操作
        if (entries != null && !entries.isEmpty()) {
            java.util.List<String> paths = new java.util.ArrayList<>(entries.size());
            java.util.List<Integer> pathApiCounts = new java.util.ArrayList<>(entries.size());
            for (ZSetOperations.TypedTuple<Object> entry : entries) {
                String path = entry.getValue() != null ? entry.getValue().toString() : null;
                if (path != null) {
                    paths.add(path);
                    pathApiCounts.add(entry.getScore() != null ? entry.getScore().intValue() : 0);
                }
            }

            // 批量获取每个路径的成功/失败计数
            java.util.List<Object> pathResults = redisTemplate.executePipelined(
                    (RedisCallback<Void>) connection -> {
                        for (String path : paths) {
                            String statusKey = STATS_API_PATH_CALL_DAY_STATUS + date + KEY_DELIM + authType + KEY_DELIM + path;
                            connection.hashCommands().hGet(statusKey.getBytes(), STATUS_SUCCESS.getBytes());
                            connection.hashCommands().hGet(statusKey.getBytes(), STATUS_ERROR.getBytes());
                        }
                        return null;
                    });

            for (int i = 0; i < paths.size(); i++) {
                String path = paths.get(i);
                int apiCount = pathApiCounts.get(i);
                Object successObj = pathResults.get(i * 2);
                int success = successObj instanceof Number ? ((Number) successObj).intValue() : 0;
                Object errorObj = pathResults.get(i * 2 + 1);
                int error = errorObj instanceof Number ? ((Number) errorObj).intValue() : 0;
                pathTotals.put(path, ApiCallResponse.builder()
                        .apiCallCount(apiCount)
                        .successCount(success)
                        .errorCount(error)
                        .build());
            }
        }

        return ApiCallDailySeriesResponse.builder()
                .date(date)
                .total(total)
                .hours(hoursMap)
                .paths(pathTotals)
                .build();
    }

}

