package com.wzkris.message.websocket.protocol;

/**
 * 协议常量
 */
public interface WsProtocol {

    /**
     * 心跳标志 ( server <-> client)
     */
    byte TYPE_HEARTBEAT = 0x01;

    /**
     * 鉴权标志 ( server <-> client)
     */
    byte TYPE_AUTH = 0x02;

    /**
     * 通知标志 ( server -> client)
     */
    byte TYPE_NOTIFICATION = 0x03;

    /**
     * 聊天标志 ( server <-> client)
     */
    byte TYPE_CHAT = 0x04;

    /**
     * 上行
     */
    byte DIRECTION_UP = 0x00;

    /**
     * 下行
     */
    byte DIRECTION_DOWN = 0x01;

    /**
     * 头部固定长
     */
    int HEADER_LENGTH = 6;

}
