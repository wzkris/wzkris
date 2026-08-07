package com.wzkris.payment.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.payment.domain.PayNotifyTaskDO;

import java.util.List;

/**
 * 业务方通知任务
 *
 * @author wzkris
 */
public interface PayNotifyTaskService extends IServicePlus<PayNotifyTaskDO> {

    /**
     * 取到期需重试的通知任务（含卡死的 SENDING）
     */
    List<PayNotifyTaskDO> findPendingRetryTasks(int limit);

    /**
     * 原子认领通知任务（PENDING/卡死SENDING -> SENDING），返回是否认领成功
     */
    boolean claimSending(Long taskId);

}
