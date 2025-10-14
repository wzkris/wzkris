package com.wzkris.common.log.event.listener;

import com.wzkris.common.core.utils.IpUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.event.OperateEvent;
import com.wzkris.common.log.report.AsyncBatchReporter;
import com.wzkris.message.feign.userlog.UserLogFeign;
import com.wzkris.message.feign.userlog.req.OperateLogReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

/**
 * 操作事件监听：单线程消费 + 多线程上报
 */
@Slf4j
public class OperateEventListener {

    private final AsyncBatchReporter<OperateLogReq> reporter;

    public OperateEventListener(UserLogFeign userLogFeign) {
        reporter = new AsyncBatchReporter<>(
                30, // 批量大小
                3,  // 定时刷出间隔（秒）
                1000, // 队列容量
                2, 5, 1000,
                "OperateEventListener-Reporter",
                logs -> {
                    try {
                        userLogFeign.saveOperlogs(logs);
                    } catch (Exception e) {
                        log.error("批量保存操作日志失败", e);
                    }
                }
        );
    }

    @EventListener
    public void onOperateEvent(OperateEvent event) {
        if (StringUtil.isNotBlank(event.getOperIp())) {
            event.setOperLocation(IpUtil.parseIp(event.getOperIp()));
        }
        reporter.submit(event.toOperateLogReq());
    }

}
