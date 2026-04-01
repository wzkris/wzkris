package com.wzkris.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzkris.system.domain.NotificationInfoDO;
import com.wzkris.system.request.message.SimpleMessageRequest;

import java.util.List;

public interface NotificationInfoService extends IService<NotificationInfoDO> {

    /**
     * 批量保存并发送在线通知
     *
     * @param adminIds   管理员ID
     * @param messageDTO 消息
     */
    void save2Admin(List<Long> adminIds, SimpleMessageRequest messageDTO);

    void save2Tenant(List<Long> memberIds, SimpleMessageRequest messageDTO);

}
