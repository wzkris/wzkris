package com.wzkris.common.log.event.listener;

import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.utils.IpUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.event.OperateEvent;
import com.wzkris.common.log.report.AsyncBatchReporter;
import com.wzkris.message.feign.tenantlog.TenantLogFeign;
import com.wzkris.message.feign.tenantlog.req.OperateLogReq;
import com.wzkris.message.feign.adminlog.AdminLogFeign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作事件监听：单线程消费 + 多线程上报
 */
@Slf4j
public class OperateEventListener {

    private final AsyncBatchReporter<OperateEvent> reporter;

    public OperateEventListener(AdminLogFeign adminLogFeign, TenantLogFeign tenantLogFeign) {
        reporter = new AsyncBatchReporter<>(
                30, // 批量大小
                3,  // 定时刷出间隔（秒）
                1000, // 队列容量
                2, 5, 1000,
                "OperateEventListener-Reporter",
                events -> {
                    List<com.wzkris.message.feign.adminlog.req.OperateLogReq> operateLogReqs = new ArrayList<>();
                    List<OperateLogReq> tenantOperateLogReqs = new ArrayList<>();
                    events.forEach(event -> {
                        if (StringUtil.isNotBlank(event.getOperIp())) {
                            event.setOperLocation(IpUtil.parseIp(event.getOperIp()));
                        }
                        if (StringUtil.equals(event.getAuthType(), AuthType.ADMIN.getValue())) {
                            operateLogReqs.add(event.toUserOperateLogReq());
                        } else if (StringUtil.equals(event.getAuthType(), AuthType.TENANT.getValue())) {
                            tenantOperateLogReqs.add(event.toTenantOperateLogReq());
                        }
                    });
                    if (CollectionUtils.isNotEmpty(operateLogReqs)) {
                        adminLogFeign.saveOperlogs(operateLogReqs);
                    }
                    if (CollectionUtils.isNotEmpty(tenantOperateLogReqs)) {
                        tenantLogFeign.saveOperlogs(tenantOperateLogReqs);
                    }
                }
        );
    }

    @EventListener
    public void onOperateEvent(OperateEvent event) {
        reporter.submit(event);
    }

}
