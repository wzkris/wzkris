package com.wzkris.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.payment.api.notify.NotifyRequest;
import com.wzkris.payment.domain.NotifyTaskDO;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.domain.RefundOrderDO;
import com.wzkris.payment.enums.notify.NotifyTaskStatusEnum;
import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import com.wzkris.payment.mapper.NotifyTaskMapper;
import com.wzkris.payment.properties.PaymentProperties;
import com.wzkris.payment.service.NotifyTaskService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * 业务方通知任务：建单投递 + 持久化 + 原子认领 + HTTP 指数退避重试
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class NotifyTaskServiceImpl
        extends ServiceImplPlus<NotifyTaskMapper, NotifyTaskDO>
        implements NotifyTaskService {

    /**
     * SENDING 超过该分钟数视为卡死（实例崩溃 mid-send），可被重新认领
     */
    private static final long STALE_SENDING_MINUTES = 5L;

    private static final int CONNECT_TIMEOUT_SECONDS = 5;

    private static final int READ_TIMEOUT_SECONDS = 10;

    private final PaymentProperties properties;

    private RestClient restClient;

    @PostConstruct
    private void init() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(CONNECT_TIMEOUT_SECONDS));
        factory.setReadTimeout(Duration.ofSeconds(READ_TIMEOUT_SECONDS));
        this.restClient = RestClient.builder().requestFactory(factory).build();
    }

    @Override
    public void createAndSend(PayOrderDO order) {
        if (order == null || StringUtil.isEmpty(order.getNotifyUrl())) {
            return;
        }
        NotifyRequest payload = buildPayNotifyRequest(order);
        NotifyTaskDO task = new NotifyTaskDO();
        task.setNotifyType(payload.getNotifyType());
        task.setPayOrderId(order.getId());
        task.setTargetUrl(order.getNotifyUrl());
        task.setPayload(JsonUtil.toJsonString(payload));
        task.setRetryCount(0);
        task.setMaxRetry(properties.getNotifyMaxRetry());
        task.setStatus(NotifyTaskStatusEnum.PENDING);
        task.setNextRetryAt(OffsetDateTime.now());
        this.save(task);

        this.send(task);
    }

    @Override
    public void createAndSend(RefundOrderDO refund) {
        if (refund == null || StringUtil.isEmpty(refund.getNotifyUrl())) {
            return;
        }
        NotifyRequest payload = buildRefundNotifyRequest(refund);
        NotifyTaskDO task = new NotifyTaskDO();
        task.setNotifyType(payload.getNotifyType());
        task.setPayOrderId(refund.getPayOrderId());
        task.setRefundOrderId(refund.getId());
        task.setTargetUrl(refund.getNotifyUrl());
        task.setPayload(JsonUtil.toJsonString(payload));
        task.setRetryCount(0);
        task.setMaxRetry(properties.getNotifyMaxRetry());
        task.setStatus(NotifyTaskStatusEnum.PENDING);
        task.setNextRetryAt(OffsetDateTime.now());
        this.save(task);

        this.send(task);
    }

    private NotifyRequest buildPayNotifyRequest(PayOrderDO order) {
        NotifyRequest req = new NotifyRequest();
        req.setNotifyType(NotifyTypeEnum.PAY);
        req.setOrderNo(order.getOrderNo());
        req.setChannel(order.getChannel());
        req.setAmount(order.getAmount());
        req.setStatus(order.getStatus());
        req.setChannelOrderNo(order.getChannelOrderNo());
        req.setPayAt(order.getPayAt());
        return req;
    }

    private NotifyRequest buildRefundNotifyRequest(RefundOrderDO refund) {
        NotifyRequest req = new NotifyRequest();
        req.setNotifyType(NotifyTypeEnum.REFUND);
        req.setOrderNo(refund.getOrderNo());
        req.setChannel(refund.getChannel());
        req.setRefundNo(refund.getRefundNo());
        req.setRefundAmount(refund.getRefundAmount());
        req.setRefundStatus(refund.getStatus());
        req.setChannelRefundNo(refund.getChannelRefundNo());
        req.setRefundAt(refund.getRefundAt());
        return req;
    }

    @Override
    public List<NotifyTaskDO> findPendingRetryTasks(int limit) {
        OffsetDateTime now = OffsetDateTime.now();
        // 到期的 PENDING，或卡死的 SENDING（update_at 早于阈值），按下次重试时间升序优先投递
        return this.list(new LambdaQueryWrapper<NotifyTaskDO>()
                .and(w -> w.eq(NotifyTaskDO::getStatus, NotifyTaskStatusEnum.PENDING)
                        .le(NotifyTaskDO::getNextRetryAt, now))
                .or(w -> w.eq(NotifyTaskDO::getStatus, NotifyTaskStatusEnum.SENDING)
                        .lt(NotifyTaskDO::getUpdateAt, now.minusMinutes(STALE_SENDING_MINUTES)))
                .orderByAsc(NotifyTaskDO::getNextRetryAt)
                .last("LIMIT " + limit));
    }

    @Override
    public boolean claimSending(Long taskId) {
        return baseMapper.claimSending(taskId, OffsetDateTime.now().minusMinutes(STALE_SENDING_MINUTES)) > 0;
    }

    @Override
    public void send(NotifyTaskDO task) {
        if (StringUtil.isEmpty(task.getTargetUrl())) {
            task.setStatus(NotifyTaskStatusEnum.FAILED);
            task.setErrorMsg("通知地址为空");
            updateById(task);
            return;
        }
        // 原子认领，防止多实例 / 重试Job / 首次投递并发重复发送
        if (!claimSending(task.getId())) {
            return;
        }
        try {
            ResponseEntity<Void> resp = restClient.post()
                    .uri(task.getTargetUrl())
                    .header("Content-Type", "application/json")
                    .body(task.getPayload())
                    .retrieve()
                    .toBodilessEntity();
            int code = resp.getStatusCode().value();
            task.setHttpStatus(code);
            if (code >= 200 && code < 300) {
                task.setStatus(NotifyTaskStatusEnum.SUCCESS);
            } else {
                markRetry(task, "HTTP " + code);
            }
        } catch (Exception e) {
            // 网络异常（连接超时/读超时/IO）无状态码，置 null 与 HTTP 错误码区分
            task.setHttpStatus(null);
            markRetry(task, e.getMessage());
        }
        updateById(task);
    }

    private void markRetry(NotifyTaskDO task, String err) {
        // retryCount/maxRetry 由 createAndSend 落库且 DDL NOT NULL，此处直接用
        int next = task.getRetryCount() + 1;
        int max = task.getMaxRetry();
        long interval = properties.getNotifyRetryIntervalSeconds() != null
                ? properties.getNotifyRetryIntervalSeconds()
                : 30L;
        task.setRetryCount(next);
        task.setErrorMsg(err);
        if (next >= max) {
            task.setStatus(NotifyTaskStatusEnum.FAILED);
        } else {
            task.setStatus(NotifyTaskStatusEnum.PENDING);
            // 指数退避：interval, 2*interval, 3*interval ...
            task.setNextRetryAt(OffsetDateTime.now().plusSeconds(interval * next));
        }
    }

}
