package com.wzkris.gateway.repository.impl;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.gateway.domain.ApiCallEventDO;
import com.wzkris.gateway.domain.ApiCallStatsDO;
import com.wzkris.gateway.repository.ApiCallRepository;
import com.wzkris.gateway.repository.impl.memory.ApiStatsBucket;
import com.wzkris.gateway.repository.impl.memory.ApiStatsWindowAccumulator;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * 基于内存的 API 统计存储实现
 *
 * <p>请求线程仅负责入队，聚合在后台批量执行，降低对主请求路径的影响。
 * 若队列已满，自动降级为当前线程同步聚合，保证不丢数据。
 *
 * <p>使用 {@link ConcurrentHashMap} + {@link LongAdder} 保证并发安全和高吞吐聚合。
 * 数据按小时/日路径粒度预聚合，结构与关系数据库的 GROUP BY 聚合或 TSDB 的降采样结果一致，
 * 切换存储后端时只需实现同一个 {@link ApiCallRepository} 接口即可。
 *
 * <p><b>内存数据保留策略</b>：每天凌晨 1 点自动清理超过 30 天的数据。
 * 重启后数据会丢失；生产环境建议替换为持久化实现。
 *
 * @author wzkris
 * @see ApiCallRepository
 */
@Slf4j
@Repository
public class InMemoryApiCallRepository implements ApiCallRepository {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter HOUR_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");

    private static final int QUEUE_CAPACITY = 20_000;

    private static final int FLUSH_BATCH_SIZE = 2_000;

    private static final int READ_DRAIN_LIMIT = 10_000;

    private static final int MAX_REALTIME_WINDOW_SECONDS = 300;

    private static final int REALTIME_RETENTION_SECONDS = 600;

    private final BlockingQueue<ApiCallEventDO> writeBuffer = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

    private final LongAdder directFallbackCount = new LongAdder();

    // key = "yyyy-MM-dd-HH:authType"
    private final ConcurrentHashMap<String, ApiStatsBucket> apiHourly = new ConcurrentHashMap<>();

    // key = "yyyy-MM-dd:authType:path"
    private final ConcurrentHashMap<String, ApiStatsBucket> apiDailyPath = new ConcurrentHashMap<>();

    // key = "epochSecond:authType"
    private final ConcurrentHashMap<String, ApiStatsBucket> apiSecondWindow = new ConcurrentHashMap<>();

    // ======== 写入 ========

    @Override
    public void recordApiCallEvent(ApiCallEventDO event) {
        if (!writeBuffer.offer(event)) {
            // 队列已满时同步写，优先保证准确性与可靠性
            directFallbackCount.increment();
            applyEvent(event);
        }
    }

    // ======== 读取 ========

    @Override
    public ApiCallStatsDO getHourlyApiCallStats(AuthTypeEnum authType, String hour) {
        drainQueue(READ_DRAIN_LIMIT);
        ApiStatsBucket agg = apiHourly.get(hour + ":" + authType.getValue());
        return agg != null ? agg.toStats() : ApiCallStatsDO.zero();
    }

    @Override
    public ApiCallStatsDO getRealtimeApiCallStats(AuthTypeEnum authType, int windowSeconds) {
        drainQueue(READ_DRAIN_LIMIT);
        int safeWindow = Math.max(1, Math.min(windowSeconds, MAX_REALTIME_WINDOW_SECONDS));
        long nowSecond = Instant.now().getEpochSecond();
        ApiStatsWindowAccumulator merged = new ApiStatsWindowAccumulator();
        for (long second = nowSecond - safeWindow + 1; second <= nowSecond; second++) {
            ApiStatsBucket agg = apiSecondWindow.get(second + ":" + authType.getValue());
            if (agg != null) {
                merged.merge(agg.toSnapshot());
            }
        }
        return merged.toStats();
    }

    @Override
    public Map<String, ApiCallStatsDO> getDailyApiCallStatsByPath(AuthTypeEnum authType, String date) {
        drainQueue(READ_DRAIN_LIMIT);
        String prefix = date + ":" + authType.getValue() + ":";
        return apiDailyPath.entrySet().stream()
                .filter(e -> e.getKey().startsWith(prefix))
                .collect(Collectors.toMap(
                        e -> e.getKey().substring(prefix.length()),
                        e -> e.getValue().toStats()
                ));
    }

    // ======== 定时清理（防止内存无限增长） ========

    /**
     * 高频批量刷新：将请求线程入队的数据批量聚合到内存桶。
     */
    @Scheduled(fixedDelay = 200)
    public void flushBufferedEvents() {
        int drained = drainQueue(FLUSH_BATCH_SIZE);
        if (drained > 0 && log.isDebugEnabled()) {
            log.debug("统计缓冲刷新完成，本轮处理 {} 条", drained);
        }
    }

    /**
     * 每天凌晨 1:00 清理过期数据
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanupExpiredData() {
        String apiExpireDate = LocalDate.now().minusDays(30).format(DATE_FMT);
        long secondExpire = Instant.now().getEpochSecond() - REALTIME_RETENTION_SECONDS;

        int removed = 0;
        removed += removeKeysOlderThan(apiDailyPath, apiExpireDate);
        removed += removeKeysOlderThan(apiHourly, apiExpireDate);
        removed += removeSecondKeysOlderThan(apiSecondWindow, secondExpire);
        log.info("API统计内存清理完成，共移除 {} 个过期桶", removed);
    }

    @PreDestroy
    public void drainOnShutdown() {
        int drained = drainQueue(Integer.MAX_VALUE);
        if (drained > 0) {
            log.info("应用停止前完成统计缓冲落盘，共处理 {} 条", drained);
        }
    }

    /**
     * 移除 key 以早于 expireDate 开头的条目（key 格式以 yyyy-MM-dd 开头）
     */
    private int removeKeysOlderThan(ConcurrentHashMap<String, ?> map, String expireDate) {
        int[] count = {0};
        map.keySet().removeIf(key -> {
            // key 格式："yyyy-MM-dd..." 或 "yyyy-MM-dd-HH..."
            String keyDate = key.length() >= 10 ? key.substring(0, 10) : key;
            if (keyDate.compareTo(expireDate) < 0) {
                count[0]++;
                return true;
            }
            return false;
        });
        return count[0];
    }

    private int drainQueue(int maxDrain) {
        int drained = 0;
        while (drained < maxDrain) {
            ApiCallEventDO event = writeBuffer.poll();
            if (event == null) {
                break;
            }
            applyEvent(event);
            drained++;
        }
        return drained;
    }

    private void applyEvent(ApiCallEventDO event) {
        String hour = event.getTimestamp().format(HOUR_FMT);
        String date = event.getTimestamp().format(DATE_FMT);
        AuthTypeEnum authType = event.getAuthType();
        String path = event.getPath();
        String method = event.getMethod();
        int statusCode = event.getStatusCode();
        long costMs = event.getCostMs();
        long second = event.getTimestamp().atZone(ZoneId.systemDefault()).toEpochSecond();

        apiHourly.computeIfAbsent(hour + ":" + authType, k -> new ApiStatsBucket())
                .record(event.isSuccess(), statusCode, costMs, method);
        apiDailyPath.computeIfAbsent(date + ":" + authType + ":" + path, k -> new ApiStatsBucket())
                .record(event.isSuccess(), statusCode, costMs, method);
        apiSecondWindow.computeIfAbsent(second + ":" + authType, k -> new ApiStatsBucket())
                .record(event.isSuccess(), statusCode, costMs, method);
    }

    private int removeSecondKeysOlderThan(ConcurrentHashMap<String, ?> map, long expireEpochSecond) {
        int[] count = {0};
        map.keySet().removeIf(key -> {
            int idx = key.indexOf(':');
            if (idx <= 0) {
                return false;
            }
            long bucketSecond;
            try {
                bucketSecond = Long.parseLong(key.substring(0, idx));
            } catch (NumberFormatException e) {
                return false;
            }
            if (bucketSecond < expireEpochSecond) {
                count[0]++;
                return true;
            }
            return false;
        });
        return count[0];
    }

    // ======== 仅用于测试/监控的辅助方法 ========

    /**
     * 快照当前时刻的统计（用于监控或诊断，勿在高频路径调用）
     */
    public Map<String, Object> snapshot() {
        return Map.of(
                "bufferSize", writeBuffer.size(),
                "directFallbackCount", directFallbackCount.sum(),
                "apiHourlyBuckets", apiHourly.size(),
                "apiDailyPathBuckets", apiDailyPath.size(),
                "apiSecondWindowBuckets", apiSecondWindow.size()
        );
    }

}
