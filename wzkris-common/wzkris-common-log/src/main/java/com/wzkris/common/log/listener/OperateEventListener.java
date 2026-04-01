package com.wzkris.common.log.listener;

import com.wzkris.common.core.utils.IpUtil;
import com.wzkris.common.core.utils.ResultUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.report.AsyncBatchReporter;
import com.wzkris.common.log.remote.IOperateLogRemote;
import com.wzkris.common.log.remote.request.OperateLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

/**
 * 操作事件监听：单线程消费 + 虚拟线程批量上报
 */
@Slf4j
public class OperateEventListener {

    private final AsyncBatchReporter<OperateLogEvent> reporter;

    public OperateEventListener(IOperateLogRemote operateLogRemote) {
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
                    if (!ResultUtil.checkNoData(operateLogRemote.save(events))) {
                        log.warn("批量上报操作日志失败, size={}", events.size());
                    }
                }
        );
    }

    @EventListener
    public void onOperateEvent(OperateLogEvent event) {
        reporter.submit(event);
    }

}
