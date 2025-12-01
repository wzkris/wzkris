package com.wzkris.common.log.report;

import com.wzkris.common.core.threads.TracingIdRunnable;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 异步批量上报器
 *
 * @param <T>
 * @author wzkris
 */
@Slf4j
public class AsyncBatchReporter<T> implements AutoCloseable {

    private final BlockingQueue<T> bufferQ;

    private final List<T> batchQ = new ArrayList<>();

    // 上报线程池
    private final ThreadPoolExecutor reporterExecutor;

    private final Consumer<List<T>> batchHandler;

    private final int batchSize;

    private final int flushIntervalSeconds;

    private final CountDownLatch consumerStopped = new CountDownLatch(1);

    private final String name;

    // 消费线程
    private Thread consumerThread;

    private volatile boolean shutdown = false;

    public AsyncBatchReporter(int batchSize,
                              int flushIntervalSeconds,
                              int queueCapacity,
                              int reporterCoreThreads,
                              int reporterMaxThreads,
                              Consumer<List<T>> batchHandler) {
        this(batchSize, flushIntervalSeconds, queueCapacity, reporterCoreThreads, reporterMaxThreads,
                100, // default reporter queue capacity
                "AsyncBatchReporter",
                batchHandler);
    }

    public AsyncBatchReporter(int batchSize,
                              int flushIntervalSeconds,
                              int queueCapacity,
                              int reporterCoreThreads,
                              int reporterMaxThreads,
                              int reporterQueueCapacity,
                              String name,
                              Consumer<List<T>> batchHandler) {
        this.batchSize = batchSize;
        this.flushIntervalSeconds = flushIntervalSeconds;
        this.batchHandler = Objects.requireNonNull(batchHandler, "batchHandler must not be null");
        this.bufferQ = new LinkedBlockingQueue<>(queueCapacity);
        this.name = (name == null || name.isEmpty()) ? "AsyncBatchReporter" : name;

        // 上报线程池 - 并发执行
        this.reporterExecutor = new ThreadPoolExecutor(
                reporterCoreThreads,
                reporterMaxThreads,
                180L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(reporterQueueCapacity),
                new ThreadFactory() {
                    final AtomicInteger tCounter = new AtomicInteger(0);

                    @Override
                    public Thread newThread(@Nonnull Runnable r) {
                        Thread t = new Thread(new TracingIdRunnable(r),
                                AsyncBatchReporter.this.name + "-Reporter-" + tCounter.incrementAndGet());
                        t.setDaemon(true);
                        return t;
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        startWorker();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("[{}] Shutdown hook triggered", AsyncBatchReporter.this.name);
            gracefulShutdown();
        }, this.name + "-ShutdownHook"));
    }

    /**
     * 提交一个待上报元素
     */
    public void submit(T item) {
        if (!shutdown) {
            if (!bufferQ.offer(item)) {
                log.warn("Buffer queue full, dropping item: {}", item);
            }
        }
    }

    /**
     * 启动后台消费线程
     */
    private void startWorker() {
        consumerThread = new Thread(new TracingIdRunnable(() -> {
            log.info("{} consumer started", name);

            while (!shutdown || !bufferQ.isEmpty()) {
                try {
                    T item = bufferQ.poll(flushIntervalSeconds, TimeUnit.SECONDS);
                    if (item == null) {
                        // 超时刷新
                        flushBatchQ();
                        if (shutdown) {
                            break;
                        }
                        continue;
                    }

                    synchronized (batchQ) {
                        batchQ.add(item);
                        if (batchQ.size() >= batchSize) {
                            flushBatchQ();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.info("{} consumer interrupted", name);
                    break;
                } catch (Exception e) {
                    log.error("{} consumer error", name, e);
                }
            }

            // 最终刷新
            flushBatchQ();
            log.info("{} consumer exited cleanly", name);
            consumerStopped.countDown();
        }), name + "-Consumer");

        consumerThread.setDaemon(true);
        consumerThread.start();
    }

    /**
     * 刷新批量上报 - 异步执行
     */
    private void flushBatchQ() {
        List<T> snapshot;
        synchronized (batchQ) {
            if (batchQ.isEmpty()) return;
            snapshot = new ArrayList<>(batchQ);
            batchQ.clear();
        }

        // 异步上报
        try {
            reporterExecutor.execute(() -> {
                try {
                    batchHandler.accept(snapshot);
                } catch (Exception e) {
                    log.error("{} flush failed", name, e);
                }
            });
        } catch (RejectedExecutionException rex) {
            // 在线程池关闭或饱和时的兜底策略
            if (shutdown) {
                return;
            }
            log.warn("{} reporter saturated, running batch in caller thread ({} items)", name, snapshot.size());
            try {
                batchHandler.accept(snapshot);
            } catch (Exception e) {
                log.error("{} fallback flush failed", name, e);
            }
        }
    }

    /**
     * 优雅关闭
     */
    private void gracefulShutdown() {
        shutdown = true;

        try {
            // 中断消费线程（如果正在阻塞等待）
            if (consumerThread != null && consumerThread.isAlive()) {
                consumerThread.interrupt();
            }

            // 等待消费线程自然完成（处理完所有积压数据）
            if (consumerThread != null) {
                consumerStopped.await();
                log.info("{} consumer thread stopped", name);
            }

            // 关闭上报线程池，等待剩余任务完成
            reporterExecutor.shutdown();
            if (!reporterExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                log.warn("{} executor not fully terminated in 30s, forcing shutdown", name);
                List<Runnable> pendingTasks = reporterExecutor.shutdownNow();
                log.warn("{} cancelled {} pending reporter tasks", name, pendingTasks.size());
            } else {
                log.info("{} executor stopped gracefully", name);
            }

            log.info("[{}] shutdown completed", name);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("{} shutdown interrupted, forcing exit", name);
            reporterExecutor.shutdownNow();
        }
    }

    @Override
    public void close() {
        gracefulShutdown();
    }

}