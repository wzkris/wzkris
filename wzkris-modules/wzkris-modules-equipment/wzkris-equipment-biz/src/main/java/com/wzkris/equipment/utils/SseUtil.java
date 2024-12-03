package com.wzkris.equipment.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SseUtil {

    static final Map<String, SseEmitter> EMITTERS = new ConcurrentHashMap<>();

    /**
     * 创建连接
     */
    public static SseEmitter connect(String uid) {
        // 设置超时时间，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
        SseEmitter sseEmitter = new SseEmitter(0L);
        //完成后回调
        sseEmitter.onCompletion(() -> {
            log.info("SSE连接[{}]结束...................", uid);
            EMITTERS.remove(uid);
        });
        //超时回调
        sseEmitter.onTimeout(() -> {
            log.info("SSE连接[{}]超时...................", uid);
            EMITTERS.remove(uid);
        });
        //异常回调
        sseEmitter.onError(
                throwable -> {
                    log.info("SSE连接[{}]异常, 异常信息：{}", uid, throwable.toString());
                    EMITTERS.remove(uid);
                }
        );
        EMITTERS.put(uid, sseEmitter);
        log.info("SSE连接[{}]创建成功", uid);
        return sseEmitter;
    }

    /**
     * 给指定用户发送消息
     */
    public static void send(String uid, Object msg) {
        String mid = String.valueOf(System.currentTimeMillis());
        send(uid, mid, msg);
    }

    /**
     * 给指定用户发送消息
     */
    public static void send(String uid, String mid, Object msg) {
        if (!EMITTERS.containsKey(uid)) {
            log.info("SSE连接[{}]消息推送失败, 没有创建连接，", uid);
            return;
        }
        SseEmitter sseEmitter = EMITTERS.get(uid);
        try {
            sseEmitter.send(SseEmitter.event()
                    .id(mid)
                    .data(msg, MediaType.APPLICATION_JSON));
        }
        catch (Exception e) {
            log.info("SSE连接[{}]消息id[{}]推送异常, 异常信息：{}", uid, mid, e.getMessage());
            disconnect(uid);
        }
    }

    /**
     * 批量发送消息
     */
    public static void sendBatch(List<String> uids, Object msg) {
        String mid = String.valueOf(System.currentTimeMillis());
        for (String uid : uids) {
            send(uid, mid, msg);
        }
    }

    /**
     * 发送所有
     */
    public static void sendAll(Object msg) {
        String mid = String.valueOf(System.currentTimeMillis());
        for (String uid : EMITTERS.keySet()) {
            send(uid, mid, msg);
        }
    }

    /**
     * 断开
     */
    public static void disconnect(String uid) {
        if (EMITTERS.containsKey(uid)) {
            SseEmitter sseEmitter = EMITTERS.get(uid);
            sseEmitter.complete();
            EMITTERS.remove(uid);
        }
    }

}
