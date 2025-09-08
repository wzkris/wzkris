package com.wzkris.system.service.impl;

import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.system.domain.NotificationInfoDO;
import com.wzkris.system.domain.NotificationToUserDO;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
import com.wzkris.system.listener.event.NotificationEvent;
import com.wzkris.system.mapper.NotificationInfoMapper;
import com.wzkris.system.mapper.NotificationToUserMapper;
import com.wzkris.system.service.NotificationInfoService;
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
                    LoginUserUtil.isAuthenticated() ? LoginUserUtil.getId() : SecurityConstants.DEFAULT_USER_ID);
            notificationInfoDO.setCreateAt(new Date());
            notificationInfoMapper.insert(notificationInfoDO);
            List<NotificationToUserDO> list = toUserIds.stream()
                    .map(uid -> new NotificationToUserDO(notificationInfoDO.getNotificationId(), uid))
                    .toList();
            notificationToUserMapper.insert(list);
        });
        SpringUtil.getContext().publishEvent(new NotificationEvent(toUserIds, messageDTO));
    }

}
