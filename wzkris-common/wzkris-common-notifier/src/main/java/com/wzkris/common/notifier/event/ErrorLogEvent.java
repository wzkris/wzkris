package com.wzkris.common.notifier.event;

import lombok.Getter;
import org.slf4j.event.Level;

/**
 * 错误日志事件
 * 用于在捕获到错误日志时发布此事件
 * 基于 slf4j 实现，不依赖具体的日志实现框架（如 Logback）
 *
 * @author wzkris
 */
@Getter
public class ErrorLogEvent {

    /**
     * 日志级别
     */
    private final Level level;

    /**
     * 日志时间戳（毫秒）
     */
    private final long timestamp;

    /**
     * 线程名
     */
    private final String threadName;

    /**
     * Logger 名称
     */
    private final String loggerName;

    /**
     * 格式化后的日志消息
     */
    private final String formattedMessage;

    /**
     * 完整格式化的日志消息（包括异常堆栈）
     * -- GETTER --
     * 获取原始消息
     * 返回结果就是打印在控制台的信息（包括格式化后的消息和异常堆栈）
     */
    private final String originalMessage;

    /**
     * 创建错误日志事件
     *
     * @param level            日志级别
     * @param timestamp        时间戳（毫秒）
     * @param threadName       线程名
     * @param loggerName       Logger 名称
     * @param formattedMessage 格式化后的消息
     * @param originalMessage  完整格式化的日志消息（包括异常堆栈）
     */
    public ErrorLogEvent(Level level, long timestamp, String threadName, String loggerName,
                         String formattedMessage, String originalMessage) {
        this.level = level;
        this.timestamp = timestamp;
        this.threadName = threadName;
        this.loggerName = loggerName;
        this.formattedMessage = formattedMessage;
        this.originalMessage = originalMessage;
    }

}

