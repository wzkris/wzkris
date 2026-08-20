package com.wzkris.payment.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.payment.domain.NotifyTaskDO;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.domain.RefundOrderDO;

import java.util.List;

/**
 * 业务方通知任务
 *
 * @author wzkris
 */
public interface NotifyTaskService extends IServicePlus<NotifyTaskDO> {

    /**
     * 支付成功通知：构建支付报文 -> 建单 -> 持久化 -> 首次投递
     *
     * @param order 支付订单（提供通知地址与支付结果；缺 notifyUrl 时静默跳过）
     */
    void createAndSend(PayOrderDO order);

    /**
     * 退款完结通知：构建退款报文 -> 建单 -> 持久化 -> 首次投递
     * <p>退款单自包含（冗余 order_no/notify_url），无需回溯原订单
     *
     * @param refund 退款订单（缺 notifyUrl 时静默跳过）
     */
    void createAndSend(RefundOrderDO refund);

    /**
     * 取到期需重试的通知任务（含卡死的 SENDING）
     */
    List<NotifyTaskDO> findPendingRetryTasks(int limit);

    /**
     * 原子认领通知任务（PENDING/卡死SENDING -> SENDING），返回是否认领成功
     */
    boolean claimSending(Long taskId);

    /**
     * 同步发送一次通知，按结果更新任务状态与重试计划
     */
    void send(NotifyTaskDO task);

}
