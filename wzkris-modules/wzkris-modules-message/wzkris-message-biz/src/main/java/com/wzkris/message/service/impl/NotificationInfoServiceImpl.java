package com.wzkris.message.service.impl;

import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.security.utils.AdminUtil;
import com.wzkris.message.domain.NotificationInfoDO;
import com.wzkris.message.domain.NotificationToAdminDO;
import com.wzkris.message.domain.NotificationToTenantDO;
import com.wzkris.message.domain.dto.SimpleMessageDTO;
import com.wzkris.message.listener.event.PubNotificationEvent;
import com.wzkris.message.mapper.NotificationInfoMapper;
import com.wzkris.message.mapper.NotificationToAdminMapper;
import com.wzkris.message.mapper.NotificationToTenantMapper;
import com.wzkris.message.service.NotificationInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationInfoServiceImpl implements NotificationInfoService {

    private final NotificationInfoMapper notificationInfoMapper;

    private final NotificationToAdminMapper notificationToAdminMapper;

    private final NotificationToTenantMapper notificationToTenantMapper;

    private final TransactionTemplate transactionTemplate;

    @Override
    public void save2Admin(List<Long> adminIds, SimpleMessageDTO messageDTO) {
        transactionTemplate.executeWithoutResult(status -> {
            NotificationInfoDO notificationInfoDO = new NotificationInfoDO();
            notificationInfoDO.setTitle(messageDTO.getTitle());
            notificationInfoDO.setNotificationType(messageDTO.getType());
            notificationInfoDO.setContent(messageDTO.getContent());
            notificationInfoDO.setCreatorId(
                    AdminUtil.isAuthenticated() ? AdminUtil.getId() : SecurityConstants.SYSTEM_USER_ID);
            notificationInfoDO.setCreateAt(new Date());
            notificationInfoMapper.insert(notificationInfoDO);
            List<NotificationToAdminDO> list = adminIds.stream()
                    .map(uid -> new NotificationToAdminDO(notificationInfoDO.getNotificationId(), uid))
                    .toList();
            notificationToAdminMapper.insert(list);
        });
        SpringUtil.getContext().publishEvent(new PubNotificationEvent(adminIds, messageDTO));
    }

    @Override
    public void save2Tenant(List<Long> memberIds, SimpleMessageDTO messageDTO) {
        transactionTemplate.executeWithoutResult(status -> {
            NotificationInfoDO notificationInfoDO = new NotificationInfoDO();
            notificationInfoDO.setTitle(messageDTO.getTitle());
            notificationInfoDO.setNotificationType(messageDTO.getType());
            notificationInfoDO.setContent(messageDTO.getContent());
            notificationInfoDO.setCreatorId(
                    AdminUtil.isAuthenticated() ? AdminUtil.getId() : SecurityConstants.SYSTEM_USER_ID);
            notificationInfoDO.setCreateAt(new Date());
            notificationInfoMapper.insert(notificationInfoDO);
            List<NotificationToTenantDO> list = memberIds.stream()
                    .map(uid -> new NotificationToTenantDO(notificationInfoDO.getNotificationId(), uid))
                    .toList();
            notificationToTenantMapper.insert(list);
        });
        SpringUtil.getContext().publishEvent(new PubNotificationEvent(memberIds, messageDTO));
    }

}
