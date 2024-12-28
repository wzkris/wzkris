package com.wzkris.system.service.impl;

import com.wzkris.system.domain.SysNotify;
import com.wzkris.system.domain.SysNotifySend;
import com.wzkris.system.mapper.SysNotifyMapper;
import com.wzkris.system.mapper.SysNotifySendMapper;
import com.wzkris.system.service.SysNotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysNotifyServiceImpl implements SysNotifyService {
    private final SysNotifyMapper notifyMapper;
    private final SysNotifySendMapper notifySendMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendNotify(List<Long> userIds, SysNotify notify) {
        int insert = notifyMapper.insert(notify);
        if (insert > 0) {
            List<SysNotifySend> list = userIds.stream()
                    .map(uid -> new SysNotifySend(uid, notify.getNotifyId())).toList();
            return notifySendMapper.insert(list) == list.size();
        }
        return false;
    }
}
