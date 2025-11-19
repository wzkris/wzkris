package com.demo.pgbus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.pgbus.controller.request.PublishMessageRequest;
import com.demo.pgbus.dal.entity.BusMessage;
import com.demo.pgbus.dal.enums.MessageStatus;
import com.demo.pgbus.dal.mapper.BusMessageMapper;
import com.demo.pgbus.service.BusMessageService;
import com.demo.pgbus.support.NotificationPayload;
import com.demo.pgbus.support.PgNotifyClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusMessageServiceImpl extends ServiceImpl<BusMessageMapper, BusMessage> implements BusMessageService {

    private final PgNotifyClient notifyClient;

    private final ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BusMessage publish(PublishMessageRequest request) {
        BusMessage message = new BusMessage();
        message.setChannel(request.getChannel());
        message.setTitle(request.getTitle());
        message.setPayload(request.getPayload());
        message.setStatus(MessageStatus.CREATED);
        save(message);

        // 在事务提交后发送通知，确保数据已持久化且通知能被监听器收到
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                dispatch(message);
            }
        });

        return message;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markDelivered(Long messageId) {
        if (messageId == null) {
            return;
        }
        BusMessage message = getById(messageId);
        if (message == null) {
            log.warn("⚠️ 消息不存在: id={}", messageId);
            return;
        }
        message.setStatus(MessageStatus.DELIVERED);
        updateById(message);
    }

    private void dispatch(BusMessage message) {
        NotificationPayload payload = new NotificationPayload(message.getId(), message.getTitle(), message.getPayload());
        try {
            String json = objectMapper.writeValueAsString(payload);
            notifyClient.notifyChannel(message.getChannel(), json);
            message.setStatus(MessageStatus.PUBLISHED);
            updateById(message);
        } catch (JsonProcessingException e) {
            message.setStatus(MessageStatus.FAILED);
            updateById(message);
            log.error("❌ 序列化消息内容失败: id={}", message.getId(), e);
        } catch (Exception e) {
            message.setStatus(MessageStatus.FAILED);
            updateById(message);
            log.error("❌ 发送 PG NOTIFY 失败: id={}", message.getId(), e);
        }
    }

}

