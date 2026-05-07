package com.wzkris.system.websocket.handler;

import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.system.enums.chat.MediaFormatEnum;
import com.wzkris.system.enums.chat.ResourceTypeEnum;
import com.wzkris.system.service.ChatPersistInfoService;
import com.wzkris.system.utils.WebSocketSessionHolder;
import com.wzkris.system.websocket.BaseWebSocketHandler;
import com.wzkris.system.websocket.protocol.ChatMessage;
import com.wzkris.system.websocket.protocol.WsMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.util.function.BiConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatHandler extends BaseWebSocketHandler {

    private final ChatPersistInfoService chatPersistInfoService;

    public void handle(WebSocketSession session, WsMessage wsMessage, BiConsumer<WebSocketSession, CloseStatus> closeSession) {
        try {
            BaseLoginUser senderInfo = getLoginInfo(session);
            ChatMessage chatMessage = ChatMessage.fromWsMessage(wsMessage);
            chatMessage.setSenderId(senderInfo.getUid());
            if (chatMessage.isText()) {
                doHandleTextMessage(senderInfo.getUid(), chatMessage);
            } else if (chatMessage.isResource()) {
                doHandleResourceMessage(senderInfo.getUid(), chatMessage);
            } else {
                log.error("处理ws聊天消息时发生错误: 未知聊天类型{}", chatMessage.getSubType());
                closeSession.accept(session, CloseStatus.BAD_DATA);
            }
        } catch (Exception e) {
            log.error("处理ws聊天消息时发生错误: {}", e.getMessage(), e);
            closeSession.accept(session, CloseStatus.BAD_DATA);
        }
    }

    private void doHandleTextMessage(Long senderId, ChatMessage chatMessage) {
        try {
            log.info("收到文本消息: 发送者={}, 接收者={}, 内容={}",
                    senderId, chatMessage.getReceiverId(), chatMessage.getText());
            deliverChatMessage(senderId, chatMessage);
        } catch (Exception e) {
            log.error("处理文本消息时发生错误: {}", e.getMessage(), e);
        }
    }

    private void doHandleResourceMessage(Long senderId, ChatMessage chatMessage) {
        try {
            log.info("收到媒体消息: 发送者={}, 接收者={}, 格式={}, 大小={}字节",
                    senderId, chatMessage.getReceiverId(), chatMessage.getMediaFormat(),
                    chatMessage.getData() != null ? chatMessage.getData().length : 0);
            deliverChatMessage(senderId, chatMessage);
        } catch (Exception e) {
            log.error("处理媒体消息时发生错误: {}", e.getMessage(), e);
        }
    }

    private void deliverChatMessage(Long senderId, ChatMessage chatMessage) throws Exception {
        if (chatMessage.isText()) {
            chatPersistInfoService.persistOutbound(senderId, chatMessage.getReceiverId(),
                    ResourceTypeEnum.TEXT, MediaFormatEnum.TEXT, chatMessage.getData());
        } else {
            chatPersistInfoService.persistOutbound(senderId, chatMessage.getReceiverId(),
                    ResourceTypeEnum.IMAGE,
                    MediaFormatEnum.fromProtocol(chatMessage.getMediaFormat()),
                    chatMessage.getData());
        }
        WebSocketSession receiverSession = WebSocketSessionHolder.getSession(chatMessage.getReceiverId());
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(chatMessage.toBinaryMessage());
        } else {
            log.debug("接收者未在线，消息已落库: receiverId={}", chatMessage.getReceiverId());
        }
    }

}
