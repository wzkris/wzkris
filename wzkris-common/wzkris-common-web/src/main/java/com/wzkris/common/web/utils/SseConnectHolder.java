package com.wzkris.common.web.utils;

import com.wzkris.common.core.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * sse连接持有工具
 */
@Slf4j
public abstract class SseConnectHolder {

    private static final Map<Serializable, SseEmitter> HOLDER = new ConcurrentHashMap<>(256);

    /**
     * @param id         指向连接
     * @param timeout    服务端过期时间 ms
     * @param onComplete 连接完成处理
     * @param onTimeout  连接超时处理
     * @param onError    连接异常处理
     * @return
     */
    public static SseEmitter connect(Serializable id, long timeout,
                                     Runnable onComplete, Runnable onTimeout, Consumer<Throwable> onError) {
        SseEmitter sseEmitter = HOLDER.get(id);
        if (sseEmitter != null) {
            return sseEmitter;
        }

        // 设置超时时间，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
        sseEmitter = new SseEmitter(timeout);
        // 完成后回调
        sseEmitter.onCompletion(() -> {
            onComplete.run();
            HOLDER.remove(id);
        });
        // 超时回调
        sseEmitter.onTimeout(() -> {
            onTimeout.run();
            HOLDER.remove(id);
        });
        // 异常回调
        sseEmitter.onError(throwable -> {
            onError.accept(throwable);
            HOLDER.remove(id);
        });
        HOLDER.put(id, sseEmitter);
        if (log.isDebugEnabled()) {
            log.debug("SSE连接:'{}'创建成功", id);
        }
        return sseEmitter;
    }

    public static void send(Serializable id, Object msg) {
        send(id, null, msg);
    }

    public static void send(Serializable id, String eventName, Object msg) {
        String mid = String.valueOf(System.currentTimeMillis());
        send(id, mid, eventName, msg);
    }

    /**
     * 给指定通道发送消息
     */
    public static void send(Serializable id, String mid, String eventName, Object msg) {
        SseEmitter sseEmitter = HOLDER.get(id);
        if (sseEmitter == null) {
            return;
        }

        if (StringUtil.isBlank(eventName)) {
            eventName = "msg";
        }
        try {
            sseEmitter.send(SseEmitter.event()
                    .id(mid)
                    .name(eventName)
                    .data(msg, MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            log.error("SSE连接:'{}'消息id:'{}'推送事件：‘{}’异常, 异常信息：{}", id, mid, eventName, e.getMessage(), e);
            sseEmitter.completeWithError(e);
        }
    }

    /**
     * 批量发送消息
     */
    public static void sendBatch(List<? extends Serializable> ids, String eventName, Object msg) {
        String mid = String.valueOf(System.currentTimeMillis());
        for (Serializable id : ids) {
            send(id, mid, eventName, msg);
        }
    }

    /**
     * 发送所有
     */
    public static void sendAll(String eventName, Object msg) {
        String mid = String.valueOf(System.currentTimeMillis());
        for (Serializable id : HOLDER.keySet()) {
            send(id, mid, eventName, msg);
        }
    }

    /**
     * 断开
     */
    public static boolean disconnect(Serializable id) {
        SseEmitter sseEmitter = HOLDER.remove(id);
        if (sseEmitter != null) {
            sseEmitter.complete();
            return true;
        }
        return false;
    }

}
