package com.wzkris.message.websocket.protocol;

import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.message.domain.dto.SimpleMessageDTO;
import lombok.Getter;
import org.springframework.web.socket.BinaryMessage;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * ws协议消息
 *
 * @author wzkris
 */
@Getter
public class WsMessage implements WsProtocol {

    private final byte type;

    private final byte direction;

    private final int length;

    private final byte[] data;

    protected WsMessage(byte type, byte direction, int length, byte[] data) {
        this.type = type;
        this.direction = direction;
        this.length = length;
        this.data = data;
    }

    // ================= 创建方法 =================
    public static WsMessage newHeartBeat() {
        return new WsMessage(
                TYPE_HEARTBEAT,
                DIRECTION_DOWN,
                0,
                new byte[0]
        );
    }

    // ================= 消息实体 =================

    public static WsMessage newChatUp(int length, byte[] payload) {
        return new WsMessage(
                TYPE_CHAT,
                DIRECTION_UP,
                length,
                payload
        );
    }

    public static WsMessage newChatDown(int length, byte[] payload) {
        return new WsMessage(
                TYPE_CHAT,
                DIRECTION_DOWN,
                length,
                payload
        );
    }

    public static WsMessage newNotification(SimpleMessageDTO messageDTO) {
        byte[] data = JsonUtil.toJsonString(messageDTO).getBytes(StandardCharsets.UTF_8);
        return new WsMessage(
                TYPE_NOTIFICATION,
                DIRECTION_DOWN,
                data.length,
                data
        );
    }

    public static WsMessage fromByteBuffer(ByteBuffer buffer) {
        if (buffer.remaining() < HEADER_LENGTH) {
            throw new IllegalArgumentException("缓冲区数据不足，无法解析消息头");
        }

        byte type = buffer.get();
        byte flags = buffer.get();
        byte direction = (byte) (flags & 0x01); // 取第一个bit
        int length = buffer.getInt(); //消息体长度
        if (buffer.remaining() < length) {
            throw new IllegalArgumentException("消息体长度不匹配");
        }
        byte[] data;
        if (length > 0) {
            data = new byte[length];
            buffer.get(data);
        } else {
            data = new byte[0];
        }

        return new WsMessage(type, direction, length, data);
    }

    /**
     * 将 WsMessage 转换为 BinaryMessage
     */
    public static BinaryMessage convertToBinaryMessage(WsMessage wsMessage) {
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_LENGTH + wsMessage.getLength());
        buffer.put(wsMessage.getType());
        buffer.put(wsMessage.getDirection());
        buffer.putInt(wsMessage.getLength());
        if (wsMessage.hasBody()) {
            buffer.put(wsMessage.getData());
        }
        buffer.flip();
        return new BinaryMessage(buffer);
    }

    // ================= 公开判断方法 =================
    public boolean isHeartbeat() {
        return type == TYPE_HEARTBEAT;
    }

    public boolean isAuth() {
        return type == TYPE_AUTH;
    }

    public boolean isNotification() {
        return type == TYPE_NOTIFICATION;
    }

    public boolean isChat() {
        return type == TYPE_CHAT;
    }

    public boolean isUpDirection() {
        return direction == DIRECTION_UP;
    }

    public boolean isDownDirection() {
        return direction == DIRECTION_DOWN;
    }

    public boolean hasBody() {
        return length > 0;
    }

}
