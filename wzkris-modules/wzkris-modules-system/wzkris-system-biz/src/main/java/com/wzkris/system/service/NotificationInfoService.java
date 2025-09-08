package com.wzkris.system.service;

import com.wzkris.system.domain.dto.SimpleMessageDTO;

import java.util.List;

public interface NotificationInfoService {

    /**
     * 批量保存并发送在线通知
     *
     * @param toUserIds  接收方用户ID
     * @param messageDTO 消息
     */
    void saveBatchAndNotify(List<Long> toUserIds, SimpleMessageDTO messageDTO);

}
