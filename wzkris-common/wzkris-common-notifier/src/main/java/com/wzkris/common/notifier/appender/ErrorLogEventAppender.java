package com.wzkris.common.notifier.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.CoreConstants;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.notifier.event.ErrorLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

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

    /**
     * 默认 pattern（当无法从 appender 获取时使用）
     */
    private static final String DEFAULT_PATTERN = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n";

    /**
     * 缓存的 pattern（null 表示未初始化，初始化后要么是从 appender 获取的 pattern，要么是默认 pattern）
     */
    private volatile String cachedPattern;

    /**
     * 缓存的 PatternLayout（null 表示未初始化）
     */
    private volatile PatternLayout cachedPatternLayout;

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
     * 格式化日志消息
     */
    private String formatLogMessage(ILoggingEvent event) {
        // 获取缓存的 PatternLayout
        PatternLayout patternLayout = getOrCreatePatternLayout();

        // PatternLayout 的 doLayout 方法直接返回格式化后的字符串
        String formattedMessage = patternLayout.doLayout(event);

        // 如果有异常，追加异常堆栈信息
        if (event.getThrowableProxy() != null) {
            StringBuilder sb = new StringBuilder(formattedMessage);
            appendThrowable(sb, event);
            return sb.toString();
        }

        return formattedMessage;
    }

    /**
     * 获取或创建 PatternLayout（带缓存）
     */
    private PatternLayout getOrCreatePatternLayout() {
        if (cachedPatternLayout != null) {
            return cachedPatternLayout;
        }

        synchronized (this) {
            if (cachedPatternLayout != null) {
                return cachedPatternLayout;
            }

            // 获取 pattern
            String pattern = getOrCreatePattern();

            // 创建并初始化 PatternLayout
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            PatternLayout patternLayout = new PatternLayout();
            patternLayout.setContext(loggerContext);
            patternLayout.setPattern(pattern);
            patternLayout.start();

            cachedPatternLayout = patternLayout;
            return patternLayout;
        }
    }

    /**
     * 获取或创建 pattern（带缓存）
     * 遍历所有 appender，获取第一个有 PatternLayoutEncoder 的 appender 的 pattern
     * 如果获取失败，直接缓存默认 pattern，避免重复尝试
     */
    private String getOrCreatePattern() {
        // 如果已缓存（包括失败时缓存的默认 pattern），直接返回
        if (cachedPattern != null) {
            return cachedPattern;
        }

        synchronized (this) {
            // 双重检查
            if (cachedPattern != null) {
                return cachedPattern;
            }

            String pattern = extractPatternFromAppenders();
            if (pattern != null) {
                cachedPattern = pattern;
                return pattern;
            }

            // 如果找不到 pattern，直接缓存默认值
            log.warn("警告：无法获取 appender 的 pattern，使用默认 pattern");
            cachedPattern = DEFAULT_PATTERN;
            return DEFAULT_PATTERN;
        }
    }

    /**
     * 从 appender 中提取 pattern
     */
    private String extractPatternFromAppenders() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger rootLogger = loggerContext.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);

        Iterator<Appender<ILoggingEvent>> appenderIterator = rootLogger.iteratorForAppenders();
        while (appenderIterator.hasNext()) {
            Appender<ILoggingEvent> appender = appenderIterator.next();
            String pattern = extractPatternFromAppender(appender);
            if (pattern != null) {
                return pattern;
            }
        }
        return null;
    }

    /**
     * 从单个 appender 中提取 pattern
     */
    private String extractPatternFromAppender(Appender<ILoggingEvent> appender) {
        ch.qos.logback.core.encoder.Encoder<?> encoder = null;

        if (appender instanceof ConsoleAppender consoleAppender) {
            encoder = consoleAppender.getEncoder();
        } else if (appender instanceof ch.qos.logback.core.rolling.RollingFileAppender fileAppender) {
            encoder = fileAppender.getEncoder();
        }

        if (encoder instanceof PatternLayoutEncoder patternEncoder) {
            return patternEncoder.getPattern();
        }

        return null;
    }

    /**
     * 追加异常堆栈信息
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
            for (StackTraceElementProxy element : stackTrace) {
                sb.append("\tat ").append(element).append(CoreConstants.LINE_SEPARATOR);
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

