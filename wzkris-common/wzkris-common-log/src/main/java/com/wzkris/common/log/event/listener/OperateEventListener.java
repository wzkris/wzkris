package com.wzkris.common.log.event.listener;

import com.wzkris.common.core.threads.TracingIdRunnable;
import com.wzkris.common.core.utils.IpUtil;
import com.wzkris.common.log.event.OperateEvent;
import com.wzkris.message.feign.userlog.UserLogFeign;
import com.wzkris.message.feign.userlog.req.OperateLogReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 操作事件监听
 *
 * @author wzkris
 * @date 2025/10/13
 */
@Slf4j
public class OperateEventListener implements ApplicationRunner {

    private static final int BATCH_SIZE = 30;

    private final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    private final UserLogFeign userLogFeign;

    private final BlockingQueue<OperateEvent> bufferQ = new LinkedBlockingQueue<>();

    private final List<OperateLogReq> batchQ = new ArrayList<>();

    private boolean shutdown = false;

    public OperateEventListener(UserLogFeign userLogFeign) {
        this.userLogFeign = userLogFeign;
        executor.setThreadNamePrefix("OperateLogAspectTask-");
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(500);
        executor.setKeepAliveSeconds(180);
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadFactory(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return executor.newThread(new TracingIdRunnable(r));
            }
        });
        executor.setDaemon(true);
        executor.initialize();
        // 注册销毁钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("[{}] Run shutdown hook now", this.getClass().getSimpleName());
            this.shutdown = true;
            executor.shutdown();
        }));
    }

    @EventListener
    public void operateEvent(OperateEvent operateEvent) {
        bufferQ.add(operateEvent);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        executor.execute(() -> {
            for (; ; ) {
                try {
                    OperateEvent operateEvent = bufferQ.poll(3, TimeUnit.SECONDS);

                    if (operateEvent == null) {
                        flushBatchQ();
                    } else {
                        operateEvent.setOperLocation(IpUtil.parseIp(operateEvent.getOperIp()));
                        batchQ.add(operateEvent.toOperateLogReq());
                        if (batchQ.size() >= BATCH_SIZE) {
                            flushBatchQ();
                        }
                    }
                } catch (InterruptedException e) {
                    if (!batchQ.isEmpty()) {
                        userLogFeign.saveOperlogs(batchQ);
                    }
                    if (shutdown) {
                        break;
                    } else {
                        log.error("操作日志的bufferQ发生异常中断：{}", e.getMessage(), e);
                    }
                }
            }
        });
    }

    /**
     * 刷新batchQ队列
     */
    private synchronized void flushBatchQ() {
        if (!batchQ.isEmpty()) {
            ArrayList<OperateLogReq> operateLogReqs = new ArrayList<>(batchQ);
            executor.execute(() -> {
                userLogFeign.saveOperlogs(operateLogReqs);
            });
            batchQ.clear();
        }
    }

}
