package com.demo.pgbus.event;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * PostgreSQL NOTIFY 事件
 * <p>
 * 封装从 PostgreSQL LISTEN/NOTIFY 机制接收到的通知消息
 * </p>
 */
@Getter
@ToString
public class PgNotifyEvent extends ApplicationEvent {

    private final String channel;

    private final String payload;

    public PgNotifyEvent(Object source, String channel, String payload) {
        super(source);
        this.channel = channel;
        this.payload = payload;
    }

}