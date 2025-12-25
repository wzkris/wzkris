package com.wzkris.common.log.event.listener;

import com.wzkris.common.core.utils.IpUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.report.AsyncBatchReporter;
import com.wzkris.system.httpservice.operatelog.OperateLogHttpService;
import com.wzkris.system.httpservice.operatelog.req.OperateLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

/**
 * 操作事件监听：单线程消费 + 多线程上报
 */
@Slf4j
public class OperateEventListener {

    private final AsyncBatchReporter<OperateLogEvent> reporter;

    public OperateEventListener(OperateLogHttpService operateLogHttpService) {
        reporter = new AsyncBatchReporter<>(
                30, // 批量大小
                3,  // 定时刷出间隔（秒）
                1000, // 队列容量
                2, 5, 1000,
                "OperateEventListener-Reporter",
                events -> {
                    events.forEach(event -> {
                        if (StringUtil.isNotBlank(event.getOperIp())) {
                            event.setOperLocation(IpUtil.parseIp(event.getOperIp()));
                        }
                    });
                    operateLogHttpService.save(events);
                }
        );
    }

    @EventListener
    public void onOperateEvent(OperateLogEvent event) {
        reporter.submit(event);
    }

}
