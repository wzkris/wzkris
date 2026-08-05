package com.wzkris.payment.service;

import com.wzkris.payment.domain.PayNotifyTaskDO;

/**
 * 业务方通知发送
 *
 * @author wzkris
 */
public interface PayNotifySenderService {

    /**
     * 同步发送一次通知，按结果更新任务状态与重试计划
     */
    void send(PayNotifyTaskDO task);
}
