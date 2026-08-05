package com.wzkris.payment.service.impl;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.payment.config.PaymentProperties;
import com.wzkris.payment.domain.PayNotifyTaskDO;
import com.wzkris.payment.enums.notify.NotifyTaskStatusEnum;
import com.wzkris.payment.service.PayNotifyTaskService;
import com.wzkris.payment.service.PayNotifySenderService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.OffsetDateTime;

/**
 * 业务方通知发送：原子认领 -> HTTP POST -> 失败指数退避重试
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class PayNotifySenderServiceImpl implements PayNotifySenderService {

    private static final int CONNECT_TIMEOUT_SECONDS = 5;

    private static final int READ_TIMEOUT_SECONDS = 10;

    private final PayNotifyTaskService taskService;

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
    public void send(PayNotifyTaskDO task) {
        if (StringUtil.isEmpty(task.getTargetUrl())) {
            task.setStatus(NotifyTaskStatusEnum.FAILED);
            task.setErrorMsg("通知地址为空");
            taskService.updateById(task);
            return;
        }
        // 原子认领，防止多实例 / 重试Job / 首次投递并发重复发送
        if (!taskService.claimSending(task.getTaskId())) {
            return;
        }
        task.setStatus(NotifyTaskStatusEnum.SENDING);
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
            markRetry(task, e.getMessage());
        }
        taskService.updateById(task);
    }

    private void markRetry(PayNotifyTaskDO task, String err) {
        int next = (task.getRetryCount() == null ? 0 : task.getRetryCount()) + 1;
        int max = task.getMaxRetry() != null ? task.getMaxRetry() : properties.getNotifyMaxRetry();
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
