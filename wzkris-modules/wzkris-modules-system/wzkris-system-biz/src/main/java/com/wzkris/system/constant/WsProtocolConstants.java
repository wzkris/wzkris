package com.wzkris.system.constant;

/**
 * ws自定义协议常量
 */
public class WsProtocolConstants {

    // 消息类型（1字节）
    public static final byte TYPE_HEARTBEAT = 0x01;    // 心跳（server/client）

    public static final byte TYPE_AUTH = 0x02;         // 认证（server/client）

    public static final byte TYPE_NOTIFICATION = 0x03; // 通知（server）

    public static final byte TYPE_CHAT = 0x04;         // 聊天（server/client）

    // 方向标志（第2字节的第1个bit）
    public static final byte DIRECTION_UP = 0x00;      // 上行（client→server）

    public static final byte DIRECTION_DOWN = 0x01;    // 下行（server→client）

    // 协议头长度
    public static final int HEADER_LENGTH = 6; // 1字节类型 + 1字节标志 + 4字节长度

}
