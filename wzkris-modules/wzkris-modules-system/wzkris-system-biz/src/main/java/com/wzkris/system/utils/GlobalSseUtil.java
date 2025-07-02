package com.wzkris.system.utils;

import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.common.web.utils.SseUtil;
import com.wzkris.system.listener.event.PublishMessageEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 扩展SSE在多节点的消息发布能力
 *
 * @author wzkris
 * @date 2025/04/17
 */
@Slf4j
public class GlobalSseUtil {

    private static final RedissonClient redissonClient = RedisUtil.getClient();

    private static final String SSE_CONNECTOR_ZSET = "global_sse_connector:zset";

    private static final String SSE_CONNECTOR_HASH = "global_sse_connector:hash";

    private static final String MSG_CHANNEL = "global_sse_msg";

    private static final String CLOSE_CHANNEL = "global_sse_close";

    private static RTopic getTopic(String topic) {
        return redissonClient.getTopic(topic);
    }

    private static RScoredSortedSet<Serializable> getConnectorsZSet() {
        return redissonClient.getScoredSortedSet(SSE_CONNECTOR_ZSET);
    }

    private static RMap<Serializable, Connector> getConnectorsHash() {
        return redissonClient.getMap(SSE_CONNECTOR_HASH);
    }

    /**
     * 分页查询连接
     */
    public static List<Connector> listConnectors(int page, int size) {
        List<Connector> result = new ArrayList<>();

        Collection<Serializable> ids = getConnectorsZSet()
                .valueRange((page - 1) * size, page * size - 1);

        RMap<Serializable, Connector> map = getConnectorsHash();
        ids.forEach(id -> {
            Connector conn = map.get(id);
            if (conn != null) result.add(conn);
        });

        // 按时间排序
        return result.stream()
                .sorted(Comparator.comparingLong(Connector::getCreateAt))
                .collect(Collectors.toList());
    }

    public static SseEmitter connect(Serializable id, long timeout, String clientName, String clientIp) {
        long currentTimeMillis = System.currentTimeMillis();

        RScoredSortedSet<Serializable> scoredSet = getConnectorsZSet();
        scoredSet.add(currentTimeMillis, id);
        scoredSet.expire(Duration.ofMillis(timeout));

        RMap<Serializable, Connector> rMap = getConnectorsHash();
        rMap.put(id, new Connector(currentTimeMillis, clientName, clientIp));
        rMap.expire(Duration.ofMillis(timeout));

        return SseUtil.connect(id, timeout,
                () -> {
                    getConnectorsZSet().remove(id);
                    getConnectorsHash().remove(id);
                },
                () -> {
                    getConnectorsZSet().remove(id);
                    getConnectorsHash().remove(id);
                },
                throwable -> {
                    getConnectorsZSet().remove(id);
                    getConnectorsHash().remove(id);
                });
    }

    public static void publish(PublishMessageEvent messageEvent) {
        getTopic(MSG_CHANNEL).publish(messageEvent);
    }

    public static void disconnect(Serializable id) {
        getTopic(CLOSE_CHANNEL).publish(id);
    }

    static {
        // 消息发布
        RTopic msgChannel = getTopic(MSG_CHANNEL);
        msgChannel.addListener(PublishMessageEvent.class, (channel, message) -> {
            if (log.isDebugEnabled()) {
                log.debug("SSE连接: {}, 收到消息{}", message.getIds(), message);
            }
            // 如果key不为空就按照key发消息 如果为空就群发
            if (CollectionUtils.isNotEmpty(message.getIds())) {
                SseUtil.sendBatch(message.getIds(), message.getMessage().getType(), message.getMessage());
            } else {
                SseUtil.sendAll(message.getMessage().getType(), message.getMessage());
            }
        });

        // 断开连接
        RTopic closeChannel = getTopic(CLOSE_CHANNEL);
        closeChannel.addListener(Serializable.class, (channel, id) -> {
            if (SseUtil.disconnect(id)) {
                if (log.isDebugEnabled()) {
                    log.debug("SSE断开连接： {}", id);
                }
                getConnectorsZSet().remove(id);
                getConnectorsHash().remove(id);
            }
        });
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Connector {

        private long createAt;

        private String clientName;

        private String clientIp;

    }

}
