package com.demo.pgbus.listener;

import com.demo.pgbus.config.PgBusProperties;
import com.demo.pgbus.event.PgNotifyEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * PostgreSQL NOTIFY 监听器
 * <p>
 * 负责监听 PostgreSQL 的 LISTEN/NOTIFY 通知，并将接收到的通知转换为 Spring 事件
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "demo.pg-bus.listener-enabled", havingValue = "true")
public class PgNotificationListener {

    private final DataSource dataSource;

    private final PgBusProperties properties;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "pg-notify-listener");
        thread.setDaemon(true);
        return thread;
    });

    private final AtomicBoolean running = new AtomicBoolean(false);

    private Connection listenerConnection;

    @PostConstruct
    public void start() {
        log.info("🚀 正在启动 PostgreSQL 通知监听器...");
        running.set(true);
        executor.submit(this::listenLoop);
    }

    @PreDestroy
    public void shutdown() {
        log.info("🛑 正在关闭 PostgreSQL 通知监听器...");
        running.set(false);
        
        // 先中断监听线程，让它从阻塞的 getNotifications 中退出
        executor.shutdownNow();
        
        // 等待线程退出，但设置超时避免无限等待
        try {
            if (!executor.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                log.warn("监听器线程未在5秒内正常退出，强制关闭连接");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("等待监听器线程退出时被中断");
        }
        
        // 最后关闭连接
        closeListenerConnection();
    }

    private void listenLoop() {
        // 验证所有要监听的 channel
        List<String> channels = properties.getChannels();
        if (channels.isEmpty()) {
            log.error("❌ 未配置监听通道");
            return;
        }

        while (running.get() && !Thread.currentThread().isInterrupted()) {
            try {
                establishAndMonitorConnection();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("监听器线程已中断");
                break;
            } catch (Exception e) {
                log.error("监听循环发生意外错误，正在重新连接...", e);
                try {
                    sleep(properties.getReconnectDelay());
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        log.info("PostgreSQL 通知监听器已停止");
    }

    private void establishAndMonitorConnection() throws InterruptedException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true); // LISTEN 需要在自动提交模式下工作

            PGConnection pgConnection = connection.unwrap(PGConnection.class);

            // 注册监听所有配置的通道
            List<String> channels = properties.getChannels();
            try (Statement stmt = connection.createStatement()) {
                for (String channel : channels) {
                    stmt.execute("LISTEN " + channel);
                    log.debug("📡 已注册监听通道: {}", channel);
                }
            }

            log.info("✅ 成功监听 {} 个通道: {}", channels.size(), channels);
            setListenerConnection(connection);

            // 进入监听循环
            monitorNotifications(pgConnection);

        } catch (SQLException e) {
            log.warn("❌ 建立数据库连接失败: {}，将在 {}ms 后重试",
                    e.getMessage(), properties.getReconnectDelay().toMillis());
            sleep(properties.getReconnectDelay());
        } finally {
            clearListenerConnection();
        }
    }

    /**
     * 监听通知（使用阻塞式调用，无需轮询和 sleep）
     * <p>
     * 使用 getNotifications(timeout) 阻塞等待，设置超时以便能够响应关闭信号
     * 这样可以消除轮询带来的 CPU 占用和延迟，同时支持优雅关闭
     * </p>
     */
    private void monitorNotifications(PGConnection pgConnection) throws SQLException, InterruptedException {
        // 使用 1 秒超时，这样可以定期检查 running 状态和中断信号
        final int timeoutMillis = 1000;
        
        while (running.get() && !Thread.currentThread().isInterrupted()) {
            try {
                // 使用超时等待，定期检查运行状态，避免无限阻塞导致无法关闭
                PGNotification[] notifications = pgConnection.getNotifications(timeoutMillis);

                if (Objects.nonNull(notifications) && notifications.length > 0) {
                    processNotifications(notifications);
                }
                // 如果超时返回 null，继续循环检查 running 状态
            } catch (SQLException e) {
                // 连接异常（如断开），抛出异常让外层处理重连
                // 但如果正在关闭，则不抛出异常，直接退出
                if (!running.get()) {
                    log.debug("监听器正在关闭，忽略连接错误");
                    break;
                }
                log.warn("等待通知时发生连接错误: {}", e.getMessage());
                throw e;
            }
        }
    }

    private void processNotifications(PGNotification[] notifications) {
        for (PGNotification notification : notifications) {
            try {
                String channelName = notification.getName();
                String payload = notification.getParameter();

                // 打印接收到的消息
                log.info("📥 [PG消息接收] Channel: {}, Payload: {}, PID: {}", channelName, payload, notification.getPID());

                // 使用事件管理器发布事件
                PgNotifyEvent event = new PgNotifyEvent(this, channelName, payload);
                applicationEventPublisher.publishEvent(event);
            } catch (Exception e) {
                log.error("❌ [PG消息处理失败]: channel={}, payload={}",
                        notification.getName(), notification.getParameter(), e);
            }
        }
    }

    private synchronized void setListenerConnection(Connection connection) {
        this.listenerConnection = connection;
    }

    private synchronized void clearListenerConnection() {
        this.listenerConnection = null;
    }

    private synchronized void closeListenerConnection() {
        if (listenerConnection != null) {
            Connection conn = listenerConnection;
            listenerConnection = null; // 先清空引用，避免重复关闭
            
            try {
                // 如果连接未关闭，尝试关闭
                if (!conn.isClosed()) {
                    // 设置超时，避免关闭时无限等待
                    try {
                        // PostgreSQL JDBC 驱动在某些情况下关闭连接可能会阻塞
                        // 这里不设置超时，但确保在单独的同步块中执行
                        conn.close();
                        log.debug("监听器连接已关闭");
                    } catch (SQLException e) {
                        // 忽略关闭时的异常，连接可能已经关闭
                        log.debug("关闭连接时发生异常（可能已关闭）: {}", e.getMessage());
                    }
                }
            } catch (SQLException e) {
                log.warn("检查连接状态时发生异常: {}", e.getMessage());
                // 即使检查失败，也尝试关闭
                try {
                    conn.close();
                } catch (SQLException closeEx) {
                    log.debug("强制关闭连接时发生异常: {}", closeEx.getMessage());
                }
            }
        }
    }

    private void sleep(Duration duration) throws InterruptedException {
        Thread.sleep(duration.toMillis());
    }

    /**
     * 检查监听器状态
     */
    public boolean isActive() {
        return running.get() && listenerConnection != null;
    }

    /**
     * 获取监听状态信息
     */
    public ListenerStatus getStatus() {
        return new ListenerStatus(
                running.get(),
                listenerConnection != null,
                properties.getChannels());
    }

    // 状态信息类
    public record ListenerStatus(
            boolean running,
            boolean connected,
            List<String> channels) {

        @Override
        public String toString() {
            return String.format("ListenerStatus{running=%s, connected=%s, channels=%s}",
                    running, connected, channels);
        }

    }

}