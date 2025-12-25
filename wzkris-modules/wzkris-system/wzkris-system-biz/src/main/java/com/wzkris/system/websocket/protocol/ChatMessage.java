package com.wzkris.system.websocket.protocol;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.BinaryMessage;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 聊天消息协议 - 精简设计
 */
@Getter
public class ChatMessage implements WsChatProtocol {

    private final byte subType;

    private final long receiverId;

    private final byte[] data;

    private final byte[] metadata; // 改为byte[]类型，支持二进制元数据

    @Setter
    private long senderId;

    public ChatMessage(byte subType, long senderId, long receiverId, byte[] data, byte[] metadata) {
        this.subType = subType;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.data = data;
        this.metadata = metadata;
    }

    // ================= 核心转换方法 =================

    public static ChatMessage fromWsMessage(WsMessage wsMessage) {
        if (!wsMessage.isChat()) {
            throw new IllegalArgumentException("不是聊天消息类型");
        }

        return parsePayload(wsMessage.getData());
    }

    private static ChatMessage parsePayload(byte[] payloadData) {
        ByteBuffer buffer = ByteBuffer.wrap(payloadData);
        byte subType = buffer.get();
        long receiverId = buffer.getLong();

        return switch (subType) {
            case SUBTYPE_TEXT -> parseTextPayload(buffer, receiverId);
            case SUBTYPE_RESOURCE -> parseMediaPayload(buffer, receiverId);
            default -> throw new IllegalArgumentException("未知的聊天消息子类型: " + subType);
        };
    }

    // ================= 载荷序列化 =================

    private static ChatMessage parseTextPayload(ByteBuffer buffer, long receiverId) {
        byte[] textData = new byte[buffer.remaining()];
        buffer.get(textData);
        return new ChatMessage(SUBTYPE_TEXT, 0, receiverId, textData, null);
    }

    // ================= 载荷解析 =================

    private static ChatMessage parseMediaPayload(ByteBuffer buffer, long receiverId) {
        byte formatLength = buffer.get();
        byte[] formatBytes = new byte[formatLength];
        buffer.get(formatBytes);

        byte[] mediaData = new byte[buffer.remaining()];
        buffer.get(mediaData);

        return new ChatMessage(WsChatProtocol.SUBTYPE_RESOURCE, 0, receiverId, mediaData, formatBytes);
    }

    // ================= 便捷方法 =================

    public BinaryMessage toBinaryMessage() {
        byte[] payloadData = serializePayload();
        WsMessage wsMessage = WsMessage.newChatDown(payloadData.length, payloadData);

        return WsMessage.convertToBinaryMessage(wsMessage);
    }

    private byte[] serializePayload() {
        ByteBuffer buffer;
        switch (subType) {
            case SUBTYPE_TEXT:
                buffer = ByteBuffer.allocate(1 + 8 + data.length);
                buffer.put(subType);
                buffer.putLong(senderId);
                buffer.put(data);
                break;
            case SUBTYPE_RESOURCE:
                buffer = ByteBuffer.allocate(1 + 8 + 1 + metadata.length + data.length);
                buffer.put(subType);
                buffer.putLong(senderId);
                buffer.put((byte) metadata.length);
                buffer.put(metadata);
                buffer.put(data);
                break;
            default:
                throw new IllegalStateException("未知的消息子类型");
        }

        return buffer.array();
    }

    public String getText() {
        if (!isText()) {
            throw new IllegalStateException("不是文本消息");
        }
        return new String(data, StandardCharsets.UTF_8);
    }

    public String getMediaFormat() {
        if (!isResource()) {
            throw new IllegalStateException("不是资源消息");
        }
        return new String(metadata, StandardCharsets.UTF_8);
    }

    // ================= 类型判断方法 =================

    public boolean isText() {
        return subType == SUBTYPE_TEXT;
    }

    public boolean isResource() {
        return subType == SUBTYPE_RESOURCE;
    }

}