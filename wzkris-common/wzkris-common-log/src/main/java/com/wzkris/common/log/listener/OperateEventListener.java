package com.wzkris.common.log.listener;

import com.wzkris.common.core.utils.IpUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.report.AsyncBatchReporter;
import com.wzkris.system.httpclient.operatelog.OperateLogClient;
import com.wzkris.system.httpclient.operatelog.req.OperateLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

/**
 * 操作事件监听：单线程消费 + 虚拟线程批量上报
 */
@Slf4j
public class OperateEventListener {

    private final AsyncBatchReporter<OperateLogEvent> reporter;

    public OperateEventListener(OperateLogClient operateLogClient) {
        this.reporter = new AsyncBatchReporter<>(
                30,   // 批量大小
                3,    // 定时刷出间隔（秒）
                1000, // 队列容量
                events -> {
                    events.forEach(event -> {
                        if (StringUtil.isNotBlank(event.getOperIp())) {
                            event.setOperLocation(IpUtil.parseIp(event.getOperIp()));
                        }
                    });
                    operateLogClient.save(events);
                }
        );
    }

    @EventListener
    public void onOperateEvent(OperateLogEvent event) {
        reporter.submit(event);
    }

}