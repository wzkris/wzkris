package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.system.domain.NotificationInfoDO;
import com.wzkris.system.domain.NotificationToAdminDO;
import com.wzkris.system.domain.NotificationToTenantDO;
import com.wzkris.system.event.PubNotificationEvent;
import com.wzkris.system.mapper.NotificationInfoMapper;
import com.wzkris.system.mapper.NotificationToAdminMapper;
import com.wzkris.system.mapper.NotificationToTenantMapper;
import com.wzkris.system.request.message.SimpleMessageRequest;
import com.wzkris.system.service.NotificationInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationInfoServiceImpl
        extends ServiceImpl<NotificationInfoMapper, NotificationInfoDO>
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
                    SecurityUtil.isAuth(AuthTypeEnum.ADMIN) ? SecurityUtil.getUid() : SecurityConstants.SYSTEM_USER_ID);
            notificationInfoDO.setCreateAt(new Date());
            baseMapper.insert(notificationInfoDO);
            List<NotificationToAdminDO> list = adminIds.stream()
                    .map(uid -> new NotificationToAdminDO(notificationInfoDO.getNotificationId(), uid))
                    .toList();
            notificationToAdminMapper.insert(list);
        });
        SpringUtil.getContext().publishEvent(new PubNotificationEvent(adminIds, messageDTO));
    }

    @Override
    public void save2Tenant(List<Long> memberIds, SimpleMessageRequest messageDTO) {
        transactionTemplate.executeWithoutResult(status -> {
            NotificationInfoDO notificationInfoDO = new NotificationInfoDO();
            notificationInfoDO.setTitle(messageDTO.getTitle());
            notificationInfoDO.setNotificationType(messageDTO.getType());
            notificationInfoDO.setContent(messageDTO.getContent());
            notificationInfoDO.setCreatorId(
                    SecurityUtil.isAuth(AuthTypeEnum.ADMIN) ? SecurityUtil.getUid() : SecurityConstants.SYSTEM_USER_ID);
            notificationInfoDO.setCreateAt(new Date());
            baseMapper.insert(notificationInfoDO);
            List<NotificationToTenantDO> list = memberIds.stream()
                    .map(uid -> new NotificationToTenantDO(notificationInfoDO.getNotificationId(), uid))
                    .toList();
            notificationToTenantMapper.insert(list);
        });
        SpringUtil.getContext().publishEvent(new PubNotificationEvent(memberIds, messageDTO));
    }

}
