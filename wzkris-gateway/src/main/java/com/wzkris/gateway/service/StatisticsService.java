package com.wzkris.gateway.service;

import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.gateway.domain.StatisticsKey;
import com.wzkris.gateway.domain.vo.ApiCallDailySeriesVO;
import com.wzkris.gateway.domain.vo.ApiCallVO;
import com.wzkris.gateway.domain.vo.PageViewDailySeriesVO;
import com.wzkris.gateway.domain.vo.PageViewVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBatch;
import org.redisson.api.RFuture;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.client.protocol.ScoredEntry;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;

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
    private static final String STATS_API_CALL_DAY = "statistics:api:day:";                 // Hash fields: {auth}

    private static final String STATS_API_CALL_DAY_STATUS = "statistics:api:status:day:";   // Hash fields: {auth}:success/error

    private static final String STATS_API_PATH_CALL_DAY = "statistics:api:path:day:";       // Hash fields: {auth}:{path}

    private static final String STATS_API_PATH_CALL_DAY_STATUS = "statistics:api:pathstatus:day:"; // Hash fields: {auth}:{path}:success/error

    private static final String STATS_API_CALL_HOUR = "statistics:api:hour:";               // Hash fields: {auth}

    private static final String STATS_API_CALL_HOUR_STATUS = "statistics:api:status:hour:"; // Hash fields: {auth}:success/error

    private static final String STATS_UV_USERS_DAY = "statistics:uv:users:day:";     // Set key: statistics:uv:users:day:{date}:{auth}

    private static final String STATS_UV_USERS_PATH_DAY = "statistics:uv:users:path:day:"; // Set key: statistics:uv:users:path:day:{date}:{auth}:{path}

    private static final String STATS_UV_USERS_HOUR = "statistics:uv:users:hour:";   // Set key: statistics:uv:users:hour:{hour}:{auth}

    // 页面真实PV埋点专用key（仅供页面埋点/TrackController使用！）
    private static final String PV_STATS_DAY = "statistics:pv:day:";      // Hash fields: {auth}

    private static final String PV_STATS_HOUR = "statistics:pv:hour:";    // Hash fields: {auth}

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
     * 记录页面PV埋点，真实页面访问量
     * 仅供 TrackController 上报调用
     */
    public void recordPV(StatisticsKey key) {
        String date = key.getDate();
        String authType = key.getAuthType();
        String hour = key.getHour();
        // 每日PV计数
        String dayPvKey = PV_STATS_DAY + date;
        incrementMapField(dayPvKey, authType, 1);
        expireMapIfNeeded(dayPvKey, java.time.Duration.ofDays(90));
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

        // 按日API调用（auth 维度）
        String dayApiCallHashKey = STATS_API_CALL_DAY + date;
        incrementMapField(dayApiCallHashKey, authType, 1);

        // 按日路径API调用量（ZSET：member=path，score=apiCall），键包含 auth
        String dayApiPathCallZsetKey = STATS_API_PATH_CALL_DAY + date + KEY_DELIM + authType;
        RScoredSortedSet<String> scored = RedisUtil.getZset(dayApiPathCallZsetKey);
        scored.addScore(path, 1D);

        // 按日成功/失败统计（键包含 auth，field 为状态）
        String dayStatusHashKey = STATS_API_CALL_DAY_STATUS + date + KEY_DELIM + authType;
        incrementMapField(dayStatusHashKey, success ? STATUS_SUCCESS : STATUS_ERROR, 1);

        // 按日路径成功/失败统计（键包含 auth 与 path，field 为状态）
        String dayPathStatusHashKey = STATS_API_PATH_CALL_DAY_STATUS + date + KEY_DELIM + authType + KEY_DELIM + path;
        incrementMapField(dayPathStatusHashKey, success ? STATUS_SUCCESS : STATUS_ERROR, 1);

        // 小时级 API 调用量与状态
        String hour = key.getHour();
        String hourApiCallHashKey = STATS_API_CALL_HOUR + hour;
        incrementMapField(hourApiCallHashKey, authType, 1);

        String hourStatusHashKey = STATS_API_CALL_HOUR_STATUS + hour + KEY_DELIM + authType;
        incrementMapField(hourStatusHashKey, success ? STATUS_SUCCESS : STATUS_ERROR, 1);

        // 过期策略：Hash 键级过期
        expireMapIfNeeded(dayApiCallHashKey, Duration.ofDays(90));
        // ZSET 过期
        expireZsetIfNeeded(dayApiPathCallZsetKey, Duration.ofDays(30));
        expireMapIfNeeded(dayStatusHashKey, Duration.ofDays(90));
        expireMapIfNeeded(dayPathStatusHashKey, Duration.ofDays(30));
        expireMapIfNeeded(hourApiCallHashKey, Duration.ofDays(7));
        expireMapIfNeeded(hourStatusHashKey, Duration.ofDays(7));
    }

    /**
     * 获取日PV埋点统计
     * 仅供页面统计调用
     */
    public int getDailyPV(String authType, String date) {
        String key = PV_STATS_DAY + date;
        Integer value = RedisUtil.getRMap(key, Integer.class).get(authType);
        return value == null ? 0 : value;
    }

    /**
     * 获取日UV统计
     */
    public int getDailyUV(String authType, String date) {
        String dayUsersSetKey = STATS_UV_USERS_DAY + date + KEY_DELIM + authType;
        int size = RedisUtil.scard(dayUsersSetKey);
        return Math.max(size, 0);
    }

    /**
     * 获取小时PV埋点统计
     * 仅供页面统计调用
     */
    public int getHourlyPV(String authType, String hour) {
        String key = PV_STATS_HOUR + hour;
        Integer value = RedisUtil.getRMap(key, Integer.class).get(authType);
        return value == null ? 0 : value;
    }

    /**
     * 获取小时UV统计
     */
    public int getHourlyUV(String authType, String hour) {
        String hourUsersSetKey = STATS_UV_USERS_HOUR + hour + KEY_DELIM + authType;
        int size = RedisUtil.scard(hourUsersSetKey);
        return Math.max(size, 0);
    }

    private void incrementMapField(String mapKey, String field, int delta) {
        RMap<String, Object> map = RedisUtil.getRMap(mapKey);
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

    private void expireZsetIfNeeded(String key, Duration duration) {
        RScoredSortedSet<?> zset = RedisUtil.getZset(key);
        long ttl = zset.remainTimeToLive();
        if (ttl <= 0) {
            zset.expire(duration);
        }
    }

    /**
     * 获取 API 调用次数统计（日）
     */
    public ApiCallVO getDailyApiCall(String authType, String date) {
        String key = STATS_API_CALL_DAY + date;
        String statusKey = STATS_API_CALL_DAY_STATUS + date + KEY_DELIM + authType;
        RMap<String, Integer> map = RedisUtil.getRMap(key);
        Integer apiCallCountNow = map.get(authType);
        int apiCallCount = apiCallCountNow == null ? 0 : apiCallCountNow;
        RMap<String, Integer> statusMap = RedisUtil.getRMap(statusKey);
        Integer successNow = statusMap.get(STATUS_SUCCESS);
        int success = successNow == null ? 0 : successNow;
        Integer errorNow = statusMap.get(STATUS_ERROR);
        int error = errorNow == null ? 0 : errorNow;
        return ApiCallVO.builder()
                .apiCallCount(apiCallCount)
                .successCount(success)
                .errorCount(error)
                .build();
    }

    /**
     * 获取页面PV/UV（日）含24小时序列
     */
    public PageViewDailySeriesVO getDailyPageViewSeries(String authType, String date) {
        java.util.Map<String, PageViewVO> hoursMap = new java.util.LinkedHashMap<>(24);

        RBatch batch = RedisUtil.createBatch();
        java.util.List<String> hourStrList = new java.util.ArrayList<>(24);
        java.util.List<RFuture<Integer>> pvFutures = new java.util.ArrayList<>(24);
        java.util.List<RFuture<Integer>> uvFutures = new java.util.ArrayList<>(24);
        for (int h = 0; h < 24; h++) {
            String hourStr = String.format("%s-%02d", date, h);
            hourStrList.add(hourStr);
            // 每小时PV取指定field
            org.redisson.api.RMapAsync<String, Integer> hourPvMap = batch.getMap(PV_STATS_HOUR + hourStr);
            pvFutures.add(hourPvMap.getAsync(authType));
            // 每小时UV使用集合大小
            org.redisson.api.RSetAsync<String> hourUvSet = batch.getSet(STATS_UV_USERS_HOUR + hourStr + KEY_DELIM + authType);
            uvFutures.add(hourUvSet.sizeAsync());
        }
        batch.execute();
        for (int i = 0; i < hourStrList.size(); i++) {
            String hourStr = hourStrList.get(i);
            Integer pvNow = pvFutures.get(i).getNow();
            int pv = pvNow == null ? 0 : pvNow;
            Integer uvNow = uvFutures.get(i).getNow();
            int uvSize = uvNow == null ? 0 : uvNow;
            int uv = Math.max(uvSize, 0);
            hoursMap.put(hourStr, PageViewVO.builder().pv(pv).uv(uv).build());
        }
        PageViewVO total = PageViewVO.builder()
                .pv(getDailyPV(authType, date))
                .uv(getDailyUV(authType, date))
                .build();
        return PageViewDailySeriesVO.builder()
                .date(date)
                .total(total)
                .hours(hoursMap)
                .build();
    }

    /**
     * 获取API调用（日）含24小时序列
     */
    public ApiCallDailySeriesVO getDailyApiCallSeries(String authType, String date) {
        java.util.Map<String, ApiCallVO> hoursMap = new java.util.LinkedHashMap<>(24);

        RBatch batch = RedisUtil.createBatch();
        java.util.List<String> hourStrList = new java.util.ArrayList<>(24);
        java.util.List<RFuture<Integer>> apiCntFutures = new java.util.ArrayList<>(24);
        java.util.List<RFuture<Integer>> successFutures = new java.util.ArrayList<>(24);
        java.util.List<RFuture<Integer>> errorFutures = new java.util.ArrayList<>(24);
        for (int h = 0; h < 24; h++) {
            String hourStr = String.format("%s-%02d", date, h);
            hourStrList.add(hourStr);
            org.redisson.api.RMapAsync<String, Integer> hourMap = batch.getMap(STATS_API_CALL_HOUR + hourStr);
            apiCntFutures.add(hourMap.getAsync(authType));
            org.redisson.api.RMapAsync<String, Integer> statusMap = batch.getMap(STATS_API_CALL_HOUR_STATUS + hourStr + KEY_DELIM + authType);
            successFutures.add(statusMap.getAsync(STATUS_SUCCESS));
            errorFutures.add(statusMap.getAsync(STATUS_ERROR));
        }
        batch.execute();
        for (int i = 0; i < hourStrList.size(); i++) {
            String hourStr = hourStrList.get(i);
            Integer apiCntNow = apiCntFutures.get(i).getNow();
            int apiCnt = apiCntNow == null ? 0 : apiCntNow;
            Integer successNow = successFutures.get(i).getNow();
            int success = successNow == null ? 0 : successNow;
            Integer errorNow = errorFutures.get(i).getNow();
            int error = errorNow == null ? 0 : errorNow;
            hoursMap.put(hourStr, ApiCallVO.builder()
                    .apiCallCount(apiCnt)
                    .successCount(success)
                    .errorCount(error)
                    .build());
        }
        ApiCallVO total = getDailyApiCall(authType, date);

        // 按路径（日）总计：来自 ZSET + 状态HASH
        String zsetKey = STATS_API_PATH_CALL_DAY + date + KEY_DELIM + authType;
        RScoredSortedSet<String> scored = RedisUtil.getZset(zsetKey);
        Collection<ScoredEntry<String>> entries = scored.entryRange(0, -1);
        java.util.Map<String, ApiCallVO> pathTotals = new java.util.LinkedHashMap<>();

        // 如果路径数量为0，直接返回空Map，避免不必要的批量操作
        if (!entries.isEmpty()) {
            // 批量获取每个路径的成功/失败计数
            RBatch pathBatch = RedisUtil.createBatch();
            java.util.List<String> paths = new java.util.ArrayList<>(entries.size());
            java.util.List<RFuture<Integer>> pathSuccessFutures = new java.util.ArrayList<>(entries.size());
            java.util.List<RFuture<Integer>> pathErrorFutures = new java.util.ArrayList<>(entries.size());
            java.util.List<Integer> pathApiCounts = new java.util.ArrayList<>(entries.size());
            for (org.redisson.client.protocol.ScoredEntry<String> e : entries) {
                String path = e.getValue();
                paths.add(path);
                pathApiCounts.add(e.getScore() == null ? 0 : e.getScore().intValue());
                String statusKey = STATS_API_PATH_CALL_DAY_STATUS + date + KEY_DELIM + authType + KEY_DELIM + path;
                org.redisson.api.RMapAsync<String, Integer> statusMap = pathBatch.getMap(statusKey);
                pathSuccessFutures.add(statusMap.getAsync(STATUS_SUCCESS));
                pathErrorFutures.add(statusMap.getAsync(STATUS_ERROR));
            }
            pathBatch.execute();
            for (int i = 0; i < paths.size(); i++) {
                String path = paths.get(i);
                int apiCount = pathApiCounts.get(i);
                Integer successNow = pathSuccessFutures.get(i).getNow();
                int success = successNow == null ? 0 : successNow;
                Integer errorNow = pathErrorFutures.get(i).getNow();
                int error = errorNow == null ? 0 : errorNow;
                pathTotals.put(path, ApiCallVO.builder()
                        .apiCallCount(apiCount)
                        .successCount(success)
                        .errorCount(error)
                        .build());
            }
        }

        return ApiCallDailySeriesVO.builder()
                .date(date)
                .total(total)
                .hours(hoursMap)
                .paths(pathTotals)
                .build();
    }

}