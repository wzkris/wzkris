package com.demo.pgbus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.pgbus.controller.request.PublishMessageRequest;
import com.demo.pgbus.dal.entity.BusMessage;

public interface BusMessageService extends IService<BusMessage> {

    BusMessage publish(PublishMessageRequest request);

    void markDelivered(Long messageId);

}

