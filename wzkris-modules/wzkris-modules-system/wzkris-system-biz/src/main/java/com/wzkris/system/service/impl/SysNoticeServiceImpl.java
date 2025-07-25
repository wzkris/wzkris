package com.wzkris.system.service.impl;

import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.system.domain.SysNotice;
import com.wzkris.system.domain.SysNoticeUser;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
import com.wzkris.system.listener.event.PublishMessageEvent;
import com.wzkris.system.mapper.SysNoticeMapper;
import com.wzkris.system.mapper.SysNoticeUserMapper;
import com.wzkris.system.service.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysNoticeServiceImpl implements SysNoticeService {

    private final SysNoticeMapper noticeMapper;

    private final SysNoticeUserMapper noticeUserMapper;

    private final TransactionTemplate transactionTemplate;

    @Override
    public void saveBatch2Users(List<Long> toUsers, SimpleMessageDTO messageDTO) {
        transactionTemplate.executeWithoutResult(status -> {
            SysNotice notice = new SysNotice();
            notice.setTitle(messageDTO.getTitle());
            notice.setNoticeType(messageDTO.getType());
            notice.setContent(messageDTO.getContent());
            notice.setCreatorId(
                    SystemUserUtil.isAuthenticated() ? SystemUserUtil.getUserId() : SecurityConstants.DEFAULT_USER_ID);
            notice.setCreateAt(new Date());
            noticeMapper.insert(notice);
            List<SysNoticeUser> list = toUsers.stream()
                    .map(uid -> new SysNoticeUser(notice.getNoticeId(), uid))
                    .toList();
            noticeUserMapper.insert(list);
        });
        SpringUtil.getContext().publishEvent(new PublishMessageEvent(toUsers, messageDTO));
    }

}
