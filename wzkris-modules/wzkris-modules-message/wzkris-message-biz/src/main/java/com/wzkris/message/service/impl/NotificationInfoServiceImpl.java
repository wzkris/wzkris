package com.wzkris.message.service.impl;

import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.security.utils.AdminUtil;
import com.wzkris.message.domain.NotificationInfoDO;
import com.wzkris.message.domain.NotificationToUserDO;
import com.wzkris.message.domain.dto.SimpleMessageDTO;
import com.wzkris.message.listener.event.PubNotificationEvent;
import com.wzkris.message.mapper.NotificationInfoMapper;
import com.wzkris.message.mapper.NotificationToUserMapper;
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

    private final NotificationToUserMapper notificationToUserMapper;

    private final TransactionTemplate transactionTemplate;

    @Override
    public void saveBatchAndNotify(List<Long> toUserIds, SimpleMessageDTO messageDTO) {
        transactionTemplate.executeWithoutResult(status -> {
            NotificationInfoDO notificationInfoDO = new NotificationInfoDO();
            notificationInfoDO.setTitle(messageDTO.getTitle());
            notificationInfoDO.setNotificationType(messageDTO.getType());
            notificationInfoDO.setContent(messageDTO.getContent());
            notificationInfoDO.setCreatorId(
                    AdminUtil.isAuthenticated() ? AdminUtil.getId() : SecurityConstants.DEFAULT_USER_ID);
            notificationInfoDO.setCreateAt(new Date());
            notificationInfoMapper.insert(notificationInfoDO);
            List<NotificationToUserDO> list = toUserIds.stream()
                    .map(uid -> new NotificationToUserDO(notificationInfoDO.getNotificationId(), uid))
                    .toList();
            notificationToUserMapper.insert(list);
        });
        SpringUtil.getContext().publishEvent(new PubNotificationEvent(toUserIds, messageDTO));
    }

}
