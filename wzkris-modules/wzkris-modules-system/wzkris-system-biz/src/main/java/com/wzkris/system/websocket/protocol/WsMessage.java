package com.wzkris.system.websocket.protocol;

import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.system.constant.WsProtocolConstants;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
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
public class WsMessage {

    private final byte type;

    private final byte direction;

    private final int length;

    private final byte[] data;

    private WsMessage(byte type, byte direction, int length, byte[] data) {
        this.type = type;
        this.direction = direction;
        this.length = length;
        this.data = data;
    }

    public static WsMessage heartBeat(byte direction) {
        return new WsMessage(
                WsProtocolConstants.TYPE_HEARTBEAT,
                direction,
                0,
                new byte[0]
        );
    }

    public static WsMessage notification(SimpleMessageDTO messageDTO) {
        byte[] data = JsonUtil.toJsonString(messageDTO).getBytes(StandardCharsets.UTF_8);
        return new WsMessage(
                WsProtocolConstants.TYPE_NOTIFICATION,
                WsProtocolConstants.DIRECTION_DOWN,
                data.length,
                data
        );
    }

    public static WsMessage fromByteBuffer(ByteBuffer buffer) {
        if (buffer.remaining() < WsProtocolConstants.HEADER_LENGTH) {
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
        ByteBuffer buffer = ByteBuffer.allocate(WsProtocolConstants.HEADER_LENGTH + wsMessage.getLength());
        buffer.put(wsMessage.getType());
        buffer.put(wsMessage.getDirection());
        buffer.putInt(wsMessage.getLength());
        if (wsMessage.hasBody()) {
            buffer.put(wsMessage.getData());
        }
        buffer.flip();
        return new BinaryMessage(buffer);
    }

    public boolean hasBody() {
        return length > 0;
    }

}
