package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.system.constant.MessageConstants;
import com.wzkris.system.domain.SysMessage;
import com.wzkris.system.domain.SysMessageSend;
import com.wzkris.system.mapper.SysMessageMapper;
import com.wzkris.system.mapper.SysMessageSendMapper;
import com.wzkris.system.service.SysMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统消息 服务层实现
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysMessageServiceImpl implements SysMessageService {
    private final SysMessageMapper messageMapper;
    private final SysMessageSendMapper messageSendMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendNotify(List<Long> userIds, Long msgId) {
        LambdaUpdateWrapper<SysMessage> luw = Wrappers.lambdaUpdate(SysMessage.class)
                .eq(SysMessage::getMsgId, msgId)
                .eq(SysMessage::getMsgType, MessageConstants.TYPE_NOTIFY)
                .eq(SysMessage::getStatus, MessageConstants.STATUS_DRAFT);
        SysMessage message = new SysMessage();
        message.setStatus(MessageConstants.STATUS_SENDED);
        if (!(messageMapper.update(message, luw) > 0)) {
            return false;
        }
        List<SysMessageSend> list = userIds.stream()
                .map(uid -> new SysMessageSend(uid, msgId)).toList();
        return messageSendMapper.insert(list) == list.size();
    }

    @Override
    public boolean checkIsNotify(Long msgId) {
        LambdaQueryWrapper<SysMessage> lqw = Wrappers.lambdaQuery(SysMessage.class)
                .eq(SysMessage::getMsgId, msgId)
                .eq(SysMessage::getMsgType, MessageConstants.TYPE_NOTIFY);
        return messageMapper.exists(lqw);
    }

    @Override
    public boolean checkIsDraft(List<Long> msgIds) {
        LambdaQueryWrapper<SysMessage> lqw = Wrappers.lambdaQuery(SysMessage.class)
                .in(SysMessage::getMsgId, msgIds)
                .eq(SysMessage::getStatus, MessageConstants.STATUS_DRAFT);
        return messageMapper.selectCount(lqw) == msgIds.size();
    }
}
