package com.wzkris.system.service.impl;

import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.system.domain.SysNotify;
import com.wzkris.system.domain.SysNotifySend;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
import com.wzkris.system.listener.event.SystemPushEvent;
import com.wzkris.system.mapper.SysNotifyMapper;
import com.wzkris.system.mapper.SysNotifySendMapper;
import com.wzkris.system.service.SysNotifyService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysNotifyServiceImpl implements SysNotifyService {

    private final SysNotifyMapper notifyMapper;

    private final SysNotifySendMapper notifySendMapper;

    private final TransactionTemplate transactionTemplate;

    @Override
    public boolean sendNotify(@Nonnull List<Long> toUsers, SimpleMessageDTO messageDTO) {
        Boolean execute = transactionTemplate.execute(status -> {
            SysNotify notify = new SysNotify();
            notify.setNotifyType(messageDTO.getType());
            notify.setTitle(messageDTO.getTitle());
            notify.setContent(messageDTO.getContent());
            notifyMapper.insert(notify);
            List<SysNotifySend> list = toUsers.stream()
                    .map(uid -> new SysNotifySend(notify.getNotifyId(), uid)).toList();
            return notifySendMapper.insert(list) > 0;
        });
        SpringUtil.getContext().publishEvent(new SystemPushEvent(toUsers, messageDTO));
        return Boolean.TRUE.equals(execute);
    }
}
