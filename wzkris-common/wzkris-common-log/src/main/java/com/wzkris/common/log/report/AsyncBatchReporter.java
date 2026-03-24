package com.wzkris.common.log.report;

import com.wzkris.common.core.threads.TracingIdRunnable;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * 异步批量上报器
 *
 * @param <T> 元素类型
 */
@Slf4j
public class AsyncBatchReporter<T> implements AutoCloseable {

    private final BlockingQueue<T> bufferQ;

    private final List<T> batchQ = new ArrayList<>();

    private final Consumer<List<T>> batchHandler;

    private final int batchSize;

    private final int flushIntervalSeconds;

    private final CountDownLatch consumerStopped = new CountDownLatch(1);

    private final String name = "operateEvent-reporter";

    private final ThreadFactory reporterThreadFactory;

    // 单消费者线程：从 bufferQ 取数据、组批
    private Thread consumerThread;

    private volatile boolean shutdown = false;

    /**
     * 最近一次加入 batchQ 的时间（纳秒，System.nanoTime）
     * 仅由消费线程读写，无需额外同步；搭配 batchQ 是否为空一起使用。
     */
    private long lastEnqueueTimeNanos = 0L;

    public AsyncBatchReporter(int batchSize,
                              int flushIntervalSeconds,
                              int queueCapacity,
                              Consumer<List<T>> batchHandler) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("batchSize must be > 0");
        }
        if (flushIntervalSeconds <= 0) {
            throw new IllegalArgumentException("flushIntervalSeconds must be > 0");
        }
        if (queueCapacity <= 0) {
            throw new IllegalArgumentException("queueCapacity must be > 0");
        }

        this.batchSize = batchSize;
        this.flushIntervalSeconds = flushIntervalSeconds;
        this.batchHandler = Objects.requireNonNull(batchHandler, "batchHandler must not be null");
        this.bufferQ = new LinkedBlockingQueue<>(queueCapacity);

        // 预先构造虚拟线程工厂，避免每次 flush 都创建 builder
        this.reporterThreadFactory = Thread
                .ofVirtual()
                .name(this.name, 0)
                .factory();

        startWorker();

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    log.info("[{}] Shutdown hook triggered", this.name);
                    gracefulShutdown();
                }, this.name + "-ShutdownHook")
        );
    }

    /**
     * 提交一个待上报元素
     */
    public void submit(T item) {
        if (shutdown) {
            log.debug("{} already shutdown, drop item: {}", name, item);
            return;
        }
        if (!bufferQ.offer(item)) {
            log.warn("{} buffer queue full, dropping item: {}", name, item);
        }
    }

    /**
     * 启动后台消费线程（平台线程，单个、长生命周期）
     */
    private void startWorker() {
        consumerThread = new Thread(new TracingIdRunnable(() -> {
            log.info("{} consumer started", name);

            try {
                final long flushIntervalNanos = TimeUnit.SECONDS.toNanos(flushIntervalSeconds);

                while (!shutdown || !bufferQ.isEmpty()) {
                    try {
                        T item;
                        if (batchQ.isEmpty()) {
                            // 没有待刷新的数据：彻底阻塞直到有新元素
                            item = bufferQ.take();
                        } else {
                            // 已有部分数据：等待“最多 flushInterval”再决定是否刷新
                            long deadline = lastEnqueueTimeNanos + flushIntervalNanos;
                            long waitNanos = deadline - System.nanoTime();
                            if (waitNanos <= 0L) {
                                flushBatchQ();
                                // 刷新后重新进入下一轮循环
                                continue;
                            }

                            item = bufferQ.poll(waitNanos, TimeUnit.NANOSECONDS);
                            if (item == null) {
                                // 在这段时间内没有新元素进来，说明“长时间未新增”，刷新现有数据
                                flushBatchQ();
                                continue;
                            }
                        }

                        boolean needFlush = false;
                        batchQ.add(item);
                        // 记录最近一次加入 batch 的时间，用于计算下一次刷新超时
                        lastEnqueueTimeNanos = System.nanoTime();
                        if (batchQ.size() >= batchSize) {
                            needFlush = true;
                        }
                        if (needFlush) {
                            flushBatchQ();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.info("{} consumer interrupted", name);
                        break;
                    } catch (Exception e) {
                        log.error("{} consumer error", name, e);
                    }
                }
            } finally {
                // 最终刷新
                flushBatchQ();
                log.info("{} consumer exited cleanly", name);
                consumerStopped.countDown();
            }
        }), name + "-Consumer");

        consumerThread.setDaemon(true);
        consumerThread.start();
    }

    /**
     * 刷新批量上报 - 使用虚拟线程异步执行
     */
    private void flushBatchQ() {
        if (batchQ.isEmpty()) {
            return;
        }
        List<T> snapshot = new ArrayList<>(batchQ);
        batchQ.clear();

        try {
            Thread t = reporterThreadFactory.newThread(
                    new TracingIdRunnable(() -> processBatch(snapshot))
            );
            t.start();
        } catch (Exception ex) {
            if (shutdown) {
                return;
            }
            log.warn("{} reporter failed to start virtual thread, running batch in caller thread ({} items)",
                    name, snapshot.size(), ex);
            processBatch(snapshot);
        }
    }

    /**
     * 实际处理批量上报
     */
    private void processBatch(List<T> snapshot) {
        try {
            batchHandler.accept(snapshot);
        } catch (Exception e) {
            log.error("{} flush failed", name, e);
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

            log.info("[{}] shutdown completed", name);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("{} shutdown interrupted, forcing exit", name);
        }
    }

    @Override
    public void close() {
        gracefulShutdown();
    }

}
