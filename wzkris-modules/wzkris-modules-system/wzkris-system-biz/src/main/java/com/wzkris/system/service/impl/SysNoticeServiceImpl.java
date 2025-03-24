package com.wzkris.system.service.impl;

import cn.hutool.core.date.DateUtil;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.security.utils.LoginUtil;
import com.wzkris.system.domain.SysNotice;
import com.wzkris.system.domain.SysNoticeUser;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
import com.wzkris.system.listener.event.SystemPushEvent;
import com.wzkris.system.mapper.SysNoticeMapper;
import com.wzkris.system.mapper.SysNoticeUserMapper;
import com.wzkris.system.service.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysNoticeServiceImpl implements SysNoticeService {

    private final SysNoticeMapper noticeMapper;

    private final SysNoticeUserMapper noticeUserMapper;

    private final TransactionTemplate transactionTemplate;

    @Override
    public boolean sendUsers(List<Long> toUsers, SimpleMessageDTO messageDTO) {
        Boolean execute = transactionTemplate.execute(status -> {
            SysNotice notice = new SysNotice();
            notice.setTitle(messageDTO.getTitle());
            notice.setNoticeType(messageDTO.getType());
            notice.setContent(messageDTO.getContent());
            notice.setCreatorId(LoginUtil.isAuthenticated() ? LoginUtil.getUserId() : SecurityConstants.DEFAULT_USER_ID);
            notice.setCreateAt(DateUtil.date());
            noticeMapper.insert(notice);
            List<SysNoticeUser> list = toUsers.stream()
                    .map(uid -> new SysNoticeUser(notice.getNoticeId(), uid)).toList();
            return noticeUserMapper.insert(list) > 0;
        });
        SpringUtil.getContext().publishEvent(new SystemPushEvent(toUsers, messageDTO));
        return Boolean.TRUE.equals(execute);
    }
}
