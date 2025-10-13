package com.wzkris.message.websocket.protocol;

public interface WsChatProtocol {

    /**
     * 文本类型
     */
    byte SUBTYPE_TEXT = 0x01;

    /**
     * 资源类型
     */
    byte SUBTYPE_RESOURCE = 0x02;

}
