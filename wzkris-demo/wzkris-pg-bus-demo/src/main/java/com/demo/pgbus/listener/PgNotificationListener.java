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
        closeListenerConnection();
        executor.shutdownNow();
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
     * 使用 getNotifications(0) 阻塞等待，直到收到通知或连接关闭
     * 这样可以消除轮询带来的 CPU 占用和延迟
     * </p>
     */
    private void monitorNotifications(PGConnection pgConnection) throws SQLException, InterruptedException {
        while (running.get() && !Thread.currentThread().isInterrupted()) {
            try {
                // 阻塞等待通知，timeout=0 表示无限等待直到收到通知或连接关闭
                PGNotification[] notifications = pgConnection.getNotifications(0);

                if (Objects.nonNull(notifications) && notifications.length > 0) {
                    processNotifications(notifications);
                }
            } catch (SQLException e) {
                // 连接异常（如断开），抛出异常让外层处理重连
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
            try {
                listenerConnection.close();
            } catch (SQLException e) {
                log.warn("关闭监听器连接失败", e);
            } finally {
                listenerConnection = null;
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