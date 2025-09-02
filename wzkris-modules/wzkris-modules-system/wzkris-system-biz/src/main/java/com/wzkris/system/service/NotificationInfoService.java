package com.wzkris.system.service;

import com.wzkris.system.domain.dto.SimpleMessageDTO;

import java.util.List;

public interface NotificationInfoService {

    /**
     * 发送通知
     *
     * @param toUsers    接收方用户ID
     * @param messageDTO 消息
     */
    void saveBatch2Users(List<Long> toUsers, SimpleMessageDTO messageDTO);

}
