package com.wzkris.common.notifier.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.CoreConstants;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.TraceIdUtil;
import com.wzkris.common.notifier.event.ErrorLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 错误日志事件 Appender（Logback 实现）
 * <p>
 * 用于捕获 ERROR 级别的日志并发布 Spring 事件。
 * <p>
 * <b>架构说明：</b>
 * <ul>
 *   <li>Appender 层：必须依赖具体的日志实现框架（当前为 Logback），因为 slf4j 只是门面，不提供 Appender 功能</li>
 *   <li>Event 层：完全基于 slf4j 标准接口，不依赖 Logback，便于扩展支持其他日志框架（如 Log4j2）</li>
 * </ul>
 * <p>
 * 如果将来需要支持其他日志框架，可以创建对应的 Appender 实现（如 ErrorLogEventAppender4j2），
 * 它们都可以发布相同的基于 slf4j 的 ErrorLogEvent。
 *
 * @author wzkris
 */
@Slf4j
public class ErrorLogEventAppender extends AppenderBase<ILoggingEvent> {

    public ErrorLogEventAppender() {
        setName("ERROR_LOG_EVENT_APPENDER");

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        setContext(loggerContext);

        start();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        // 只处理 ERROR 级别的日志
        if (eventObject.getLevel() != Level.ERROR) {
            return;
        }

        // 过滤掉通知相关的 Logger，避免循环
        String loggerName = eventObject.getLoggerName();
        if (loggerName != null && loggerName.startsWith("com.wzkris.common.notifier")) {
            return;
        }

        // 检查 Spring 上下文是否已初始化
        if (SpringUtil.getContext() == null) {
            return;
        }

        // 格式化日志消息
        String originalMessage = formatLogMessage(eventObject);

        // 转换日志级别为 slf4j Level
        org.slf4j.event.Level level = convertToSlf4jLevel(eventObject.getLevel());

        // 构建错误日志事件（基于 slf4j，不依赖 Logback）
        ErrorLogEvent errorLogEvent = new ErrorLogEvent(
                level,
                eventObject.getTimeStamp(),
                eventObject.getThreadName(),
                eventObject.getLoggerName(),
                eventObject.getFormattedMessage(),
                originalMessage
        );

        // 发布 Spring 事件
        SpringUtil.getContext().publishEvent(errorLogEvent);
    }

    /**
     * 手动格式化日志消息（不依赖PatternLayout）
     */
    private String formatLogMessage(ILoggingEvent event) {
        StringBuilder sb = new StringBuilder();

        // 1. 时间戳
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append(sdf.format(new Date(event.getTimeStamp())))
                .append(" ");

        // 2. 线程名（在方括号中）
        sb.append("[")
                .append(event.getThreadName())
                .append("] ")
                .append(" ");

        if (TraceIdUtil.get() != null) {
            sb.append(TraceIdUtil.get())
                    .append(" ");
        }

        // 3. 日志级别（右对齐，最小宽度5）
        String levelStr = event.getLevel().toString();
        sb.append(String.format("%-5s", levelStr))
                .append(" ");

        // 4. Logger名称
        sb.append(event.getLoggerName());

        // 5. 分隔符和消息
        sb.append(" - ")
                .append(event.getFormattedMessage())
                .append(CoreConstants.LINE_SEPARATOR);

        // 6. 如果有异常，追加异常堆栈信息（清理ANSI代码并限制堆栈）
        if (event.getThrowableProxy() != null) {
            appendThrowable(sb, event);
        }

        return sb.toString();
    }

    /**
     * 追加异常堆栈信息（限制堆栈深度）
     */
    private void appendThrowable(StringBuilder sb, ILoggingEvent event) {
        if (event.getThrowableProxy() == null) {
            return;
        }

        sb.append(event.getThrowableProxy().getClassName())
                .append(": ")
                .append(event.getThrowableProxy().getMessage())
                .append(CoreConstants.LINE_SEPARATOR);

        StackTraceElementProxy[] stackTrace = event.getThrowableProxy().getStackTraceElementProxyArray();
        if (stackTrace != null) {
            // 限制堆栈深度
            int maxDepth = 20;

            for (int i = 0; i < maxDepth; i++) {
                // 修复：直接使用element.toString()，它已经包含了"at "
                sb.append("\t").append(stackTrace[i]).append(CoreConstants.LINE_SEPARATOR);
            }

            // 如果堆栈被截断，添加提示
            if (stackTrace.length > maxDepth) {
                sb.append("\t")
                        .append("... ").append(stackTrace.length - maxDepth)
                        .append(" 行堆栈已省略，完整信息请查看日志文件")
                        .append(CoreConstants.LINE_SEPARATOR);
            }
        }
    }

    /**
     * 转换 Logback Level 为 slf4j Level
     */
    private org.slf4j.event.Level convertToSlf4jLevel(ch.qos.logback.classic.Level level) {
        if (level == ch.qos.logback.classic.Level.TRACE) {
            return org.slf4j.event.Level.TRACE;
        }
        if (level == ch.qos.logback.classic.Level.DEBUG) {
            return org.slf4j.event.Level.DEBUG;
        }
        if (level == ch.qos.logback.classic.Level.INFO) {
            return org.slf4j.event.Level.INFO;
        }
        if (level == ch.qos.logback.classic.Level.WARN) {
            return org.slf4j.event.Level.WARN;
        }
        if (level == ch.qos.logback.classic.Level.ERROR) {
            return org.slf4j.event.Level.ERROR;
        }
        return org.slf4j.event.Level.INFO;
    }

}

