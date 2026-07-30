package com.wzkris.usercenter.service.impl;

import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.usercenter.api.notification.request.SimpleMessageRequest;
import com.wzkris.usercenter.domain.NotificationInfoDO;
import com.wzkris.usercenter.domain.NotificationToAdminDO;
import com.wzkris.usercenter.domain.NotificationToTenantDO;
import com.wzkris.usercenter.mapper.NotificationInfoMapper;
import com.wzkris.usercenter.mapper.NotificationToAdminMapper;
import com.wzkris.usercenter.mapper.NotificationToTenantMapper;
import com.wzkris.usercenter.service.NotificationInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationInfoServiceImpl
        extends ServiceImplPlus<NotificationInfoMapper, NotificationInfoDO>
        implements NotificationInfoService {

    private final NotificationToAdminMapper notificationToAdminMapper;

    private final NotificationToTenantMapper notificationToTenantMapper;

    private final TransactionTemplate transactionTemplate;

    @Override
    public void save2Admin(List<Long> adminIds, SimpleMessageRequest messageDTO) {
        transactionTemplate.executeWithoutResult(status -> {
            NotificationInfoDO notificationInfoDO = new NotificationInfoDO();
            notificationInfoDO.setTitle(messageDTO.getTitle());
            notificationInfoDO.setNotificationType(messageDTO.getType());
            notificationInfoDO.setContent(messageDTO.getContent());
            notificationInfoDO.setCreatorId(
                    SecurityUtil.isAuth() ? SecurityUtil.getUid() : SecurityConstants.SYSTEM_USER_ID);
            notificationInfoDO.setCreateAt(OffsetDateTime.now());
            baseMapper.insert(notificationInfoDO);
            List<NotificationToAdminDO> list = adminIds.stream()
                    .map(uid -> new NotificationToAdminDO(notificationInfoDO.getNotificationId(), uid))
                    .toList();
            notificationToAdminMapper.insert(list);
        });
    }

    @Override
    public void save2Tenant(List<Long> memberIds, SimpleMessageRequest messageDTO) {
        transactionTemplate.executeWithoutResult(status -> {
            NotificationInfoDO notificationInfoDO = new NotificationInfoDO();
            notificationInfoDO.setTitle(messageDTO.getTitle());
            notificationInfoDO.setNotificationType(messageDTO.getType());
            notificationInfoDO.setContent(messageDTO.getContent());
            notificationInfoDO.setCreatorId(
                    SecurityUtil.isAuth() ? SecurityUtil.getUid() : SecurityConstants.SYSTEM_USER_ID);
            notificationInfoDO.setCreateAt(OffsetDateTime.now());
            baseMapper.insert(notificationInfoDO);
            List<NotificationToTenantDO> list = memberIds.stream()
                    .map(uid -> new NotificationToTenantDO(notificationInfoDO.getNotificationId(), uid))
                    .toList();
            notificationToTenantMapper.insert(list);
        });
    }

}
