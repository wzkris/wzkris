package com.wzkris.system.listener;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.system.constant.MessageConstants;
import com.wzkris.system.constant.SystemPushEventName;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
import com.wzkris.system.listener.event.SystemPushAlertEvent;
import com.wzkris.system.utils.SseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 系统消息推送
 * @since : 2024/12/28 16:50
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemPushAlertListener {

    @Async
    @EventListener
    public void loginEvent(SystemPushAlertEvent alertEvent) {
        List<?> ids = alertEvent.getIds();
        SimpleMessageDTO messageDTO = alertEvent.getMessageDTO();
        String eventName = null;
        ObjectNode msg = JsonUtil.createObjectNode();
        msg.put("title", messageDTO.getTitle());
        msg.put("content", messageDTO.getContent());

        doSystemPush(messageDTO.getType(), eventName, ids, msg);
    }

    private static void doSystemPush(String type, String eventName, List<?> ids, ObjectNode msg) {
        switch (type) {
            case MessageConstants.NOTIFY_TYPE_SYSTEM -> eventName = SystemPushEventName.SYSTEM_NOTIFY;
            case MessageConstants.NOTIFY_TYPE_DEVICE -> eventName = SystemPushEventName.DEVICE_NOTIFY;
        }
        SseUtil.sendBatch(ids, eventName, msg);
    }
}
