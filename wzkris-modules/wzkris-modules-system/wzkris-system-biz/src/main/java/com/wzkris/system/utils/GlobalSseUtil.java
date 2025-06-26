package com.wzkris.system.utils;

import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.common.web.utils.SseUtil;
import com.wzkris.system.listener.event.PublishMessageEvent;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 扩展SSE在多节点的消息发布能力
 *
 * @author wzkris
 * @date 2025/04/17
 */
@Slf4j
@Component
public class GlobalSseUtil {

    private static final String GLOBAL_CHANNEL = "global_sse";

    private static final String CLOSE_CHANNEL = "global_sse:close";

    private static RTopic getTopic(String topic) {
        return RedisUtil.getClient().getTopic(topic);
    }

    public static void publish(PublishMessageEvent messageEvent) {
        getTopic(GLOBAL_CHANNEL).publish(messageEvent);
    }

    public static void disconnect(Object id) {
        getTopic(CLOSE_CHANNEL).publish(id);
    }

    @PostConstruct
    public void subscribe() {
        // 消息发布
        RTopic globalChannel = RedisUtil.getClient().getTopic(GLOBAL_CHANNEL);
        globalChannel.addListener(PublishMessageEvent.class, (channel, message) -> {
            log.info("SSE发布主题收到消息{}", message);
            // 如果key不为空就按照key发消息 如果为空就群发
            if (!CollectionUtils.isEmpty(message.getIds())) {
                SseUtil.sendBatch(message.getIds(), message.getMessage().getType(), message.getMessage());
            } else {
                SseUtil.sendAll(message.getMessage().getType(), message.getMessage());
            }
        });

        // 断开连接
        RTopic closeChannel = RedisUtil.getClient().getTopic(CLOSE_CHANNEL);
        closeChannel.addListener(Object.class, (channel, id) -> {
            log.info("SSE断开连接主题收到消息{}", id);
            SseUtil.disconnect(id);
        });
    }

}
