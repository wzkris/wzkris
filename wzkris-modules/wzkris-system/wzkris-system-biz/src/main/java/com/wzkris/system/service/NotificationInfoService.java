package com.wzkris.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzkris.system.api.notification.request.SimpleMessageRequest;
import com.wzkris.system.domain.NotificationInfoDO;

import java.util.List;

public interface NotificationInfoService extends IService<NotificationInfoDO> {

    void save2Admin(List<Long> adminIds, SimpleMessageRequest messageDTO);

    void save2Tenant(List<Long> memberIds, SimpleMessageRequest messageDTO);

}
