package com.wzkris.common.log.event.listener;

import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.utils.IpUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.event.OperateEvent;
import com.wzkris.common.log.report.AsyncBatchReporter;
import com.wzkris.message.feign.stafflog.StaffLogFeign;
import com.wzkris.message.feign.userlog.UserLogFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

/**
 * 操作事件监听：单线程消费 + 多线程上报
 */
@Slf4j
public class OperateEventListener {

    private final AsyncBatchReporter<OperateEvent> reporter;

    public OperateEventListener(UserLogFeign userLogFeign, StaffLogFeign staffLogFeign) {
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
                        if (StringUtil.equals(event.getAuthType(), AuthType.USER.getValue())) {
                        } else if (StringUtil.equals(event.getAuthType(), AuthType.STAFF.getValue())) {
                        }
                    });
                }
        );
    }

    @EventListener
    public void onOperateEvent(OperateEvent event) {
        reporter.submit(event);
    }

}
