package com.wzkris.common.web.utils;

import com.wzkris.common.core.utils.StringUtil;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SseUtil {

    private static final Map<Object, SseEmitter> EMITTERS = new ConcurrentHashMap<>();

    /**
     * 创建连接
     */
    public static SseEmitter connect(Object id) {
        SseEmitter sseEmitter = EMITTERS.get(id);
        if (sseEmitter != null) {
            return sseEmitter;
        }

        // 设置超时时间，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
        sseEmitter = new SseEmitter(0L);
        // 完成后回调
        sseEmitter.onCompletion(() -> {
            log.info("SSE连接:'{}'结束...................", id);
            EMITTERS.remove(id);
        });
        // 超时回调
        sseEmitter.onTimeout(() -> {
            log.info("SSE连接:'{}'超时...................", id);
            EMITTERS.remove(id);
        });
        // 异常回调
        sseEmitter.onError(throwable -> {
            log.info("SSE连接:'{}'异常, 异常信息：{}", id, throwable.toString());
            EMITTERS.remove(id);
        });
        EMITTERS.put(id, sseEmitter);
        log.info("SSE连接:'{}'创建成功", id);
        return sseEmitter;
    }

    public static void send(Object id, Object msg) {
        send(id, null, msg);
    }

    public static void send(Object id, String eventName, Object msg) {
        String mid = String.valueOf(System.currentTimeMillis());
        send(id, mid, eventName, msg);
    }

    /**
     * 给指定通道发送消息
     */
    public static void send(Object id, String mid, String eventName, Object msg) {
        SseEmitter sseEmitter = EMITTERS.get(id);
        if (sseEmitter == null) {
            return;
        }

        if (StringUtil.isBlank(eventName)) {
            eventName = "message";
        }
        try {
            sseEmitter.send(SseEmitter.event()
                    .id(mid)
                    .name(eventName)
                    .reconnectTime(3000)
                    .data(msg, MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            log.info("SSE连接:'{}'消息id:'{}'推送事件：‘{}’异常, 异常信息：{}", id, mid, eventName, e.getMessage());
        }
    }

    /**
     * 批量发送消息
     */
    public static void sendBatch(List<?> ids, String eventName, Object msg) {
        String mid = String.valueOf(System.currentTimeMillis());
        for (Object id : ids) {
            send(id, mid, eventName, msg);
        }
    }

    /**
     * 发送所有
     */
    public static void sendAll(String eventName, Object msg) {
        String mid = String.valueOf(System.currentTimeMillis());
        for (Object id : EMITTERS.keySet()) {
            send(id, mid, eventName, msg);
        }
    }

    /**
     * 断开
     */
    public static void disconnect(Object id) {
        SseEmitter sseEmitter = EMITTERS.remove(id);
        if (sseEmitter != null) {
            sseEmitter.complete();
        }
    }
}
