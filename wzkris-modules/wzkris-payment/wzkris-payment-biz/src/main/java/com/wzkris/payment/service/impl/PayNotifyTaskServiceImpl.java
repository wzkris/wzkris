package com.wzkris.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.payment.domain.PayNotifyTaskDO;
import com.wzkris.payment.enums.notify.NotifyTaskStatusEnum;
import com.wzkris.payment.mapper.PayNotifyTaskMapper;
import com.wzkris.payment.service.PayNotifyTaskService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class PayNotifyTaskServiceImpl
        extends ServiceImplPlus<PayNotifyTaskMapper, PayNotifyTaskDO>
        implements PayNotifyTaskService {

    /** SENDING 超过该分钟数视为卡死（实例崩溃 mid-send），可被重新认领 */
    private static final long STALE_SENDING_MINUTES = 5L;

    @Override
    public List<PayNotifyTaskDO> findPendingRetryTasks(int limit) {
        OffsetDateTime now = OffsetDateTime.now();
        // 到期的 PENDING，或卡死的 SENDING（update_at 早于阈值）
        return this.list(new LambdaQueryWrapper<PayNotifyTaskDO>()
                .and(w -> w.eq(PayNotifyTaskDO::getStatus, NotifyTaskStatusEnum.PENDING)
                        .le(PayNotifyTaskDO::getNextRetryAt, now))
                .or(w -> w.eq(PayNotifyTaskDO::getStatus, NotifyTaskStatusEnum.SENDING)
                        .lt(PayNotifyTaskDO::getUpdateAt, now.minusMinutes(STALE_SENDING_MINUTES)))
                .last("LIMIT " + limit));
    }

    @Override
    public boolean claimSending(Long taskId) {
        return baseMapper.claimSending(taskId, OffsetDateTime.now().minusMinutes(STALE_SENDING_MINUTES)) > 0;
    }
}
