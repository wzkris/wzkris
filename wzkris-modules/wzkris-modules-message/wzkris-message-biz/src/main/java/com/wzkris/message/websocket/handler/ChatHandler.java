package com.wzkris.message.websocket.handler;

import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.message.domain.UserChatMessageDO;
import com.wzkris.message.mapper.UserChatMessageMapper;
import com.wzkris.message.utils.WebSocketSessionHolder;
import com.wzkris.message.websocket.BaseWebSocketHandler;
import com.wzkris.message.websocket.protocol.ChatMessage;
import com.wzkris.message.websocket.protocol.WsMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;
import java.util.function.BiConsumer;

/**
 * 聊天处理
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatHandler extends BaseWebSocketHandler {

    private final UserChatMessageMapper chatMessageMapper;

    public void handle(WebSocketSession session, WsMessage wsMessage, BiConsumer<WebSocketSession, CloseStatus> closeSession) {
        try {
            MyPrincipal senderInfo = getLoginInfo(session);

            // 解析聊天消息
            ChatMessage chatMessage = ChatMessage.fromWsMessage(wsMessage);
            chatMessage.setSenderId(senderInfo.getId());

            // 根据消息类型处理
            if (chatMessage.isText()) {
                doHandleTextMessage(senderInfo.getId(), chatMessage);
            } else if (chatMessage.isResource()) {
                doHandleImageMessage(senderInfo.getId(), chatMessage);
            } else if (chatMessage.isResource()) {
                // do sth.
            } else {
                // 未知消息类型
                log.error("处理ws聊天消息时发生错误: 未知聊天类型{}", chatMessage.getSubType());
                closeSession.accept(session, CloseStatus.BAD_DATA);
            }
        } catch (Exception e) {
            log.error("处理ws聊天消息时发生错误: {}", e.getMessage(), e);
            closeSession.accept(session, CloseStatus.BAD_DATA);
        }
    }

    /**
     * 处理文本消息
     */
    private void doHandleTextMessage(Long senderId, ChatMessage chatMessage) {
        try {
            log.info("收到文本消息: 发送者={}, 接收者={}, 内容={}",
                    senderId, chatMessage.getReceiverId(), chatMessage.getText());

            WebSocketSession receiverSession = WebSocketSessionHolder.getSession(chatMessage.getReceiverId());
            if (receiverSession == null || !receiverSession.isOpen()) {
                // 接收者没有socket需要存储
                storeMessage(senderId, chatMessage, "text");
            } else {
                receiverSession.sendMessage(chatMessage.toBinaryMessage());
            }
        } catch (Exception e) {
            log.error("处理文本消息时发生错误: {}", e.getMessage(), e);
        }
    }

    /**
     * 处理图片消息
     */
    private void doHandleImageMessage(Long senderId, ChatMessage chatMessage) {
        try {
            log.info("收到图片消息: 发送者={}, 接收者={}, 格式={}, 大小={}字节",
                    senderId, chatMessage.getReceiverId(), chatMessage.getMediaFormat(), chatMessage.getMetadata().length);

            WebSocketSession receiverSession = WebSocketSessionHolder.getSession(chatMessage.getReceiverId());
            if (receiverSession == null || !receiverSession.isOpen()) {
                // 接收者没有socket需要存储
                storeMessage(senderId, chatMessage, "image");
            } else {
                receiverSession.sendMessage(chatMessage.toBinaryMessage());
            }

        } catch (Exception e) {
            log.error("处理图片消息时发生错误: {}", e.getMessage(), e);
        }
    }

    /**
     * 保存消息到数据库
     */
    private void storeMessage(Long senderId, ChatMessage chatMessage, String messageType) {
        try {
            UserChatMessageDO message = new UserChatMessageDO();
            message.setSenderId(senderId);
            message.setReceiverId(chatMessage.getReceiverId());
            message.setSendTime(new Date());
            message.setMessageType(messageType);
            message.setRead(false);
            message.setContent(chatMessage.getData());

            if (chatMessage.isText()) {
                message.setMediaFormat("text");
            } else {
                message.setMediaFormat(chatMessage.getMediaFormat());
            }

            chatMessageMapper.insert(message);
            log.debug("消息已保存到数据库: senderId={}, receiverId={}, type={}",
                    senderId, chatMessage.getReceiverId(), chatMessage.getSubType());

        } catch (Exception e) {
            log.error("保存消息到数据库时发生错误: {}", e.getMessage(), e);
        }
    }

}