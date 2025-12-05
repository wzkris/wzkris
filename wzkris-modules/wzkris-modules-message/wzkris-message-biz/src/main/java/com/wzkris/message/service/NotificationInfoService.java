package com.wzkris.message.service;

import com.wzkris.message.domain.dto.SimpleMessageDTO;

import java.util.List;

public interface NotificationInfoService {

    /**
     * 批量保存并发送在线通知
     *
     * @param adminIds   管理员ID
     * @param messageDTO 消息
     */
    void save2Admin(List<Long> adminIds, SimpleMessageDTO messageDTO);

    void save2Tenant(List<Long> memberIds, SimpleMessageDTO messageDTO);

}
