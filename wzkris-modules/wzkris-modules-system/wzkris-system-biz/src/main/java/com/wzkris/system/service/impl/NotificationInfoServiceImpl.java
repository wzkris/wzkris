package com.wzkris.system.service.impl;

import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.system.domain.NotificationInfoDO;
import com.wzkris.system.domain.NotificationToUserDO;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
import com.wzkris.system.listener.event.PublishMessageEvent;
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
    public void saveBatch2Users(List<Long> toUsers, SimpleMessageDTO messageDTO) {
        transactionTemplate.executeWithoutResult(status -> {
            NotificationInfoDO notice = new NotificationInfoDO();
            notice.setTitle(messageDTO.getTitle());
            notice.setNotificationType(messageDTO.getType());
            notice.setContent(messageDTO.getContent());
            notice.setCreatorId(
                    LoginUserUtil.isAuthenticated() ? LoginUserUtil.getId() : SecurityConstants.DEFAULT_USER_ID);
            notice.setCreateAt(new Date());
            notificationInfoMapper.insert(notice);
            List<NotificationToUserDO> list = toUsers.stream()
                    .map(uid -> new NotificationToUserDO(notice.getNotificationId(), uid))
                    .toList();
            notificationToUserMapper.insert(list);
        });
        SpringUtil.getContext().publishEvent(new PublishMessageEvent(toUsers, messageDTO));
    }

}
